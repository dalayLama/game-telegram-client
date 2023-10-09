package org.quizstorage.services;

import lombok.Getter;
import org.apache.commons.lang3.RandomUtils;
import org.quizstoradge.director.dto.AnswerResult;
import org.quizstoradge.director.dto.GameInfo;
import org.quizstoradge.director.dto.GameQuestionDto;
import org.quizstoradge.director.dto.GameResult;
import org.quizstorage.exceptions.GameIsFinishedExcepiton;
import org.quizstorage.exceptions.GameNotFoundException;
import org.quizstorage.generator.dto.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.IntStream;

@Service
public class MockQuizStorageApiService implements QuizStorageApiService {

    private final ConcurrentMap<Long, GameHolder> games = new ConcurrentHashMap<>();

    private final ConcurrentMap<Long, List<GameHolder>> finishedGames = new ConcurrentHashMap<>();

    @Override
    public List<QuizSourceDto> getSourcesList() {
        return List.of(
                new QuizSourceDto("1", "Source 1", "description"),
                new QuizSourceDto("2", "Source 2", "description 2")
        );
    }

    @Override
    public List<InitField<?>> getInitFields(String sourceId) {
        return List.of(
                FieldType.SELECT.createInitField("multiSelect", "Choose a category", false,
                        new SelectFormat(true, List.of(new SelectOption("1", "option1"), new SelectOption("2", "option2")))
                ),
                FieldType.SELECT.createInitField("simpleSelect", "Select an option", false,
                        new SelectFormat(false, List.of(new SelectOption("1", "option1"), new SelectOption("2", "option2")))
                ),
                FieldType.NUMBER.createInitField("number1", "fill number", false, new NumberFormat(10, 5)),
                FieldType.NUMBER.createInitField("number2", "fill number", false, null)
        );
    }

    @Override
    public QuestionSet generateQuestionSet(String sourceId, Map<String, Object> config) {
        return new QuestionSet(sourceId, List.of(
                new Question(
                        "question 1",
                        Set.of("answer 1", "answer 2"),
                        Set.of("answer 1"),
                        "games",
                        false,
                        Difficulty.EASY
                ),
                new Question(
                        "question 2",
                        Set.of("answer 1", "answer 2"),
                        Set.of("answer 2"),
                        "games",
                        false,
                        Difficulty.EASY
                ),
                new Question(
                        "question 3",
                        Set.of("answer 1", "answer 2", "answer 3", "answer 4"),
                        Set.of("answer 1", "answer 2"),
                        "games",
                        true,
                        Difficulty.EASY
                )
        ));
    }

    @Override
    public GameInfo createGame(Long userId, QuestionSet questionSet) {
        GameInfo gameInfo = new GameInfo(
                UUID.randomUUID().toString(),
                userId.toString(),
                questionSet.sourceId(),
                Instant.now(),
                null
        );
        GameHolder gameHolder = new GameHolder(gameInfo, questionSet);
        games.put(userId, gameHolder);
        return gameInfo;
    }

    @Override
    public Optional<GameQuestionDto> getCurrentQuestion(Long userId, String gameId) {
        GameHolder gameHolder = getGameHolder(userId);
        return Optional.ofNullable(gameHolder.getCurrentQuestion());
    }

        @Override
    public AnswerResult acceptAnswer(Long userId, String gameId, int questionNumber, Set<String> answers) {
            GameHolder gameHolder = getGameHolder(userId);
            AnswerResult result = gameHolder.closeCurrentQuestion();
            if (result.nextQuestion() == null) {
                List<GameHolder> list = new ArrayList<>();
                list.add(gameHolder);
                finishedGames.merge(userId, list, (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                });
            }
            return result;
        }

    @Override
    public GameResult getGameResult(Long userId, String gameId) {
        GameHolder finishedGameHolder = getFinishedGameHolder(userId, gameId);
        return finishedGameHolder.getGameResult();
    }

    @Override
    public List<GameInfo> findFinishedGames(Long userId) {
        List<GameHolder> finishedGames = this.finishedGames.getOrDefault(userId, Collections.emptyList());
        return finishedGames.stream()
                .map(GameHolder::getGameInfo)
                .toList();
    }

    private GameHolder getGameHolder(Long userId) {
        GameHolder gameHolder = games.get(userId);
        if (gameHolder == null) {
            throw new GameNotFoundException("Game not found for user: " + userId);
        }
        return gameHolder;
    }

    private GameHolder getFinishedGameHolder(Long userId, String gameId) {
        List<GameHolder> gameHolders = finishedGames.get(userId);
        if (gameHolders == null) {
            throw new GameNotFoundException("Finished game not found for user: " + userId + " and gameId: " + gameId);
        }
        return gameHolders.stream()
                .filter(gameHolder -> gameHolder.getGameInfo().id().equals(gameId))
                .findFirst()
                .orElseThrow(() -> new GameNotFoundException("Finished game not found for user: " + userId + " and gameId: " + gameId));
    }

    @Getter
    private static class GameHolder {

        private GameInfo gameInfo;

        private final Queue<GameQuestionDto> questions;

        private final List<AnswerResult> answers;

        private GameQuestionDto currentQuestion;

        public GameHolder(GameInfo gameInfo, QuestionSet questions) {
            this.gameInfo = gameInfo;
            this.questions = new ArrayDeque<>();
            this.questions.addAll(toGameQuestionDtos(questions.questions()));
            this.answers = new ArrayList<>(this.questions.size());
            this.currentQuestion = this.questions.poll();
        }

        public synchronized AnswerResult closeCurrentQuestion() {
            if (currentQuestion != null) {
                int number = currentQuestion.number();
                currentQuestion = this.questions.poll();
                AnswerResult result = new AnswerResult(gameInfo, number, currentQuestion);
                this.answers.add(result);
                if (currentQuestion == null) {
                    gameInfo = new GameInfo(
                            gameInfo.id(),
                            gameInfo.userId(),
                            gameInfo.sourceId(),
                            gameInfo.start(),
                            Instant.now()
                    );
                }
                return result;
            } else {
                throw new GameIsFinishedExcepiton("Game %s is already finished".format(gameInfo.id()));
            }
        }

        public GameResult getGameResult() {
            return new GameResult(questions.size(), RandomUtils.nextInt(0, questions.size()));
        }

        private List<GameQuestionDto> toGameQuestionDtos(List<? extends Question> questions) {
            return IntStream.range(0, questions.size())
                    .mapToObj(number -> toGameQuestionDto(number + 1, questions.get(number)))
                    .toList();
        }

        private GameQuestionDto toGameQuestionDto(int number, Question question) {
            return new GameQuestionDto(
                    this.gameInfo,
                    number,
                    question.question(),
                    question.answers(),
                    question.category(),
                    question.multiplyAnswers()
            );
        }

    }

}
