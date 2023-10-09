package org.quizstorage.services;

import org.quizstoradge.director.dto.AnswerResult;
import org.quizstoradge.director.dto.GameInfo;
import org.quizstoradge.director.dto.GameQuestionDto;
import org.quizstoradge.director.dto.GameResult;
import org.quizstorage.generator.dto.InitField;
import org.quizstorage.generator.dto.QuestionSet;
import org.quizstorage.generator.dto.QuizSourceDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface QuizStorageApiService {
    List<QuizSourceDto> getSourcesList();

    List<InitField<?>> getInitFields(String sourceId);

    QuestionSet generateQuestionSet(String sourceId, Map<String, Object> config);

    GameInfo createGame(Long userId, QuestionSet questionSet);

    Optional<GameQuestionDto> getCurrentQuestion(Long userId, String gameId);

    AnswerResult acceptAnswer(Long userId, String gameId, int questionNumber, Set<String> answers);

    GameResult getGameResult(Long userId, String gameId);

    List<GameInfo> findFinishedGames(Long userId);

}
