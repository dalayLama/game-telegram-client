package org.quizstorage.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.quizstoradge.director.dto.AnswerResult;
import org.quizstoradge.director.dto.GameInfo;
import org.quizstoradge.director.dto.GameQuestionDto;
import org.quizstoradge.director.dto.GameResult;
import org.quizstorage.components.common.initfield.InitFieldValueValidator;
import org.quizstorage.components.satemachine.QuizStateMachine;
import org.quizstorage.components.satemachine.QuizStorageStateMachineFacade;
import org.quizstorage.components.satemachine.QuizUserEvent;
import org.quizstorage.components.telegram.DialogService;
import org.quizstorage.exceptions.*;
import org.quizstorage.generator.dto.*;
import org.quizstorage.objects.InitFieldValueHolder;
import org.quizstorage.objects.InitFieldsContainer;
import org.quizstorage.utils.TelegramMessageUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultGameService implements GameService {

    private final QuizStorageApiService apiService;

    private final QuizStorageStateMachineFacade stateMachineFacade;

    private final DialogService dialogService;

    private final InitFieldValueValidator valueValidator;

    @Override
    public void newGame(Long userId) {
        QuizStateMachine stateMachine = stateMachineFacade.getStateMachine();
        stateMachine.start();
        List<QuizSourceDto> sourcesList = apiService.getSourcesList();
        stateMachine.setSourcesList(sourcesList);
        stateMachine.sendEvent(QuizUserEvent.SELECT_SOURCE);
        stateMachineFacade.save(stateMachine, userId);
        dialogService.askToSelectSource(userId, sourcesList);
    }

    @Override
    public void selectSourceByName(Message message) {
        Long userId = message.getFrom().getId();
        QuizStateMachine stateMachine = stateMachineFacade.getStateMachine(userId);
        QuizSourceDto source = stateMachine
                .findSource(s -> Objects.equals(s.name(), message.getText()))
                .orElseThrow(() -> new SourceNotFoundException(message.getText()));
        dialogService.confirmSelectedSource(userId, source);
        initGame(userId, source.id(), stateMachine);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setCurrentInitFieldValue(Message message) {
        Long userId = message.getFrom().getId();
        QuizStateMachine stateMachine = stateMachineFacade.getStateMachine(userId);
        Pair<InitFieldsContainer, InitFieldValueHolder> pair = stateMachine.getCurrentValueHolder()
                .orElseThrow(CurrentInitFieldValueHolderIsAbsent::new);
        InitFieldValueHolder valueHolder = pair.getValue();
        boolean selection = valueHolder.getInitField().type() == FieldType.SELECT;
        String actualValue = selection
                ? extractSelectValue((InitField<SelectFormat>) valueHolder.getInitField(), message.getText())
                : message.getText();
        valueValidator.validate(valueHolder.getInitField(), actualValue);
        valueHolder.setValue(actualValue);
        dialogService.confirmInitField(message, valueHolder.getInitField());
        stateMachineFacade.save(stateMachine, userId);
        pair.getKey().nextInitField().ifPresentOrElse(
                nextInitField -> dialogService.askToSetInitField(userId, nextInitField),
                () -> startGame(userId, stateMachine)
        );
    }

    @Override
    public void setCurrentInitFieldValues(CallbackQuery callbackQuery) {
        Long userId = callbackQuery.getFrom().getId();
        QuizStateMachine stateMachine = stateMachineFacade.getStateMachine(userId);
        Pair<InitFieldsContainer, InitFieldValueHolder> pair = stateMachine.getCurrentValueHolder()
                .orElseThrow(CurrentInitFieldValueHolderIsAbsent::new);
        InitFieldValueHolder initFieldValueHolder = pair.getValue();
        List<String> values = dialogService.getSelectedValues(callbackQuery.getMessage().getReplyMarkup());
        valueValidator.validate(initFieldValueHolder.getInitField(), values);
        initFieldValueHolder.setValue(values);
        stateMachineFacade.save(stateMachine, userId);
        dialogService.confirmInitField(callbackQuery, initFieldValueHolder.getInitField());
        pair.getKey().nextInitField().ifPresentOrElse(
                nextInitField -> dialogService.askToSetInitField(userId, nextInitField),
                () -> startGame(userId, stateMachine)
        );
    }

    @Override
    public void skipCurrentInitField(CallbackQuery callbackQuery) {
        Long userId = callbackQuery.getFrom().getId();
        QuizStateMachine stateMachine = stateMachineFacade.getStateMachine(userId);
        Pair<InitFieldsContainer, InitFieldValueHolder> pair = stateMachine.getCurrentValueHolder()
                .orElseThrow(CurrentInitFieldValueHolderIsAbsent::new);
        InitFieldValueHolder initFieldValueHolder = pair.getValue();
        InitField<?> initField = initFieldValueHolder.getInitField();
        if (initField.required()) {
            throw new RequiredInitFieldException(initField.name());
        }
        initFieldValueHolder.setSkipped(true);
        stateMachineFacade.save(stateMachine, userId);
        dialogService.confirmSkipping(callbackQuery, initField);
        pair.getKey().nextInitField().ifPresentOrElse(
                nextInitField -> dialogService.askToSetInitField(userId, nextInitField),
                () -> startGame(userId, stateMachine)
        );
    }

    @Override
    public void skipCurrentInitField(Message message) {
        Long userId = message.getFrom().getId();
        QuizStateMachine stateMachine = stateMachineFacade.getStateMachine(userId);
        Pair<InitFieldsContainer, InitFieldValueHolder> pair = stateMachine.getCurrentValueHolder()
                .orElseThrow(CurrentInitFieldValueHolderIsAbsent::new);
        InitFieldValueHolder initFieldValueHolder = pair.getValue();
        InitField<?> initField = initFieldValueHolder.getInitField();
        if (initField.required()) {
            throw new RequiredInitFieldException(initField.name());
        }
        initFieldValueHolder.setSkipped(true);
        stateMachineFacade.save(stateMachine, userId);
        dialogService.confirmSkipping(message, initField);
        pair.getKey().nextInitField().ifPresentOrElse(
                nextInitField -> dialogService.askToSetInitField(userId, nextInitField),
                () -> startGame(userId, stateMachine)
        );
    }

    @Override
    public void acceptAnswer(Message message) {
        Long userId = message.getFrom().getId();
        QuizStateMachine stateMachine = stateMachineFacade.getStateMachine(userId);
        GameQuestionDto question = getCurrentQuestion(userId, stateMachine)
                .orElseThrow(CurrentQuestionIsUnknownException::new);
        AnswerResult result = apiService.acceptAnswer(
                userId, question.gameInfo().id(), question.number(), Set.of(message.getText()));
        stateMachine.setCurrentQuestion(result.nextQuestion());
        stateMachineFacade.save(stateMachine, userId);
        dialogService.confirmAnswer(message, question, result);
        Optional.ofNullable(result.nextQuestion()).ifPresentOrElse(
                nextQuestion -> dialogService.askQuestion(userId, nextQuestion),
                () -> finishGame(userId, result.gameInfo().id(), stateMachine)
        );
    }

    @Override
    public void acceptAnswers(CallbackQuery callbackQuery) {
        Long userId = callbackQuery.getFrom().getId();
        QuizStateMachine stateMachine = stateMachineFacade.getStateMachine(userId);
        GameQuestionDto question = getCurrentQuestion(userId, stateMachine)
                .orElseThrow(CurrentQuestionIsUnknownException::new);
        List<String> values = dialogService.getSelectedValues(callbackQuery.getMessage().getReplyMarkup());
        AnswerResult answer = apiService.acceptAnswer(
                userId, question.gameInfo().id(), question.number(), new HashSet<>(values));
        stateMachine.setCurrentQuestion(answer.nextQuestion());
        stateMachineFacade.save(stateMachine, userId);
        dialogService.confirmAnswer(callbackQuery, question, answer);
        Optional.ofNullable(answer.nextQuestion()).ifPresentOrElse(
                nextQuestion -> dialogService.askQuestion(userId, nextQuestion),
                () -> finishGame(userId, answer.gameInfo().id(), stateMachine)
        );
    }

    private String extractSelectValue(InitField<SelectFormat> initField, String messageText) {
        return TelegramMessageUtils.findOptionByDescription(initField.conf(), messageText)
                .map(SelectOption::id)
                .orElseThrow(() -> new NotSelectedOptionException(initField.name()));
    }

    private void initGame(Long userId,
                          String selectedSourceId,
                          QuizStateMachine stateMachine) {
        List<InitField<?>> fields = apiService.getInitFields(selectedSourceId);
        InitFieldsContainer initFieldsContainer = new InitFieldsContainer(fields);
        stateMachine.setSourceId(selectedSourceId);
        stateMachine.setInitFieldsContainer(initFieldsContainer);
        stateMachine.sendEvent(QuizUserEvent.FILL_INIT_DATA);
        stateMachineFacade.save(stateMachine, userId);
        initFieldsContainer.nextInitField().ifPresentOrElse(
                initField -> dialogService.askToSetInitField(userId, initField),
                () -> startGame(userId, stateMachine)
        );
    }

    private void startGame(Long userId, QuizStateMachine stateMachine) {
        String sourceId = stateMachine.getSourceId().orElseThrow(QuizSourceIsNotDeterminedException::new);
        InitFieldsContainer container = stateMachine.getInitFieldsContainer()
                .orElseThrow(InitFieldsContainerIsAbsentException::new);
        Map<String, Object> config = container.toMap();
        QuestionSet questionSet = apiService.generateQuestionSet(sourceId, config);
        GameInfo game = apiService.createGame(userId, questionSet);
        stateMachine.setCurrentGame(game);
        stateMachine.sendEvent(QuizUserEvent.START_GAME);
        stateMachineFacade.save(stateMachine, userId);
        askCurrentQuestion(userId, game.id(), stateMachine);
    }

    private void askCurrentQuestion(Long userId, String gameId, QuizStateMachine stateMachine) {
        stateMachine.getCurrentQuestion().or(() -> loadCurrentQuestion(userId, gameId, stateMachine)).ifPresentOrElse(
                question -> dialogService.askQuestion(userId, question),
                () -> finishGame(userId, gameId, stateMachine)
        );
    }

    private void finishGame(Long userId, String gameId, QuizStateMachine stateMachine) {
        stateMachine.sendEvent(QuizUserEvent.FINISH_GAME);
        stateMachineFacade.save(stateMachine, userId);
        GameResult gameResult = apiService.getGameResult(userId, gameId);
        dialogService.gameOver(userId, gameResult);
    }

    private Optional<GameQuestionDto> getCurrentQuestion(Long userId, QuizStateMachine stateMachine) {
        return stateMachine.getCurrentQuestion().or(() -> {
            GameInfo gameInfo = stateMachine.getCurrentGame().orElseThrow(CurrentGameIsNotDeterminedException::new);
            return loadCurrentQuestion(userId, gameInfo.id(), stateMachine);
        });
    }

    private Optional<GameQuestionDto> loadCurrentQuestion(Long userId, String gameId, QuizStateMachine stateMachine) {
        Optional<GameQuestionDto> currentQuestion = apiService.getCurrentQuestion(userId, gameId);
        currentQuestion.ifPresent(q -> {
            stateMachine.setCurrentQuestion(q);
            stateMachineFacade.save(stateMachine, userId);
        });
        return currentQuestion;
    }

}
