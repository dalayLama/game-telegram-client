package org.quizstorage.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quizstorage.components.common.initfield.InitFieldValueValidator;
import org.quizstorage.components.satemachine.QuizStorageStateMachineFacade;
import org.quizstorage.components.satemachine.QuizUserEvent;
import org.quizstorage.components.satemachine.QuizUserState;
import org.quizstorage.components.telegram.DialogService;
import org.quizstorage.exceptions.*;
import org.quizstorage.generator.dto.*;
import org.quizstorage.objects.InitFieldValueHolder;
import org.quizstorage.objects.InitFieldsContainer;
import org.quizstorage.utils.TelegramMessageUtils;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        StateMachine<QuizUserState, QuizUserEvent> stateMachine = stateMachineFacade.getStateMachine();
        if (stateMachine.isComplete()) {
            stateMachine.start();
        }
        List<QuizSourceDto> sourcesList = apiService.getSourcesList();
        stateMachine.getExtendedState().getVariables().put("sourcesList", sourcesList);
        stateMachine.sendEvent(QuizUserEvent.SELECT_SOURCE);
        stateMachineFacade.save(stateMachine, userId);
        dialogService.askToSelectSource(userId, sourcesList);
    }

    @Override
    public void selectSourceByName(Message message) {
        Long userId = message.getFrom().getId();
        StateMachine<QuizUserState, QuizUserEvent> stateMachine = stateMachineFacade.getStateMachine(userId);
        QuizSourceDto source = findSourceByName(message.getText(), stateMachine)
                .orElseThrow(() -> new SourceNotFoundException(message.getText()));
        dialogService.confirmSelectedSource(userId, source);
        initGame(userId, source.id(), stateMachine);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setCurrentInitFieldValue(Message message) {
        Long userId = message.getFrom().getId();
        StateMachine<QuizUserState, QuizUserEvent> stateMachine = stateMachineFacade.getStateMachine(userId);
        InitFieldsContainer container = getInitFieldsContainer(stateMachine);
        InitFieldValueHolder initFieldValueHolder = getInitFieldValueHolder(container);
        boolean selection = initFieldValueHolder.getInitField().type() == FieldType.SELECT;
        String  actualValue = selection
                ? extractSelectValue((InitField<SelectFormat>) initFieldValueHolder.getInitField(), message.getText())
                : message.getText();
        valueValidator.validate(initFieldValueHolder.getInitField(), actualValue);
        initFieldValueHolder.setValue(actualValue);
        dialogService.confirmInitField(message, initFieldValueHolder.getInitField());
        stateMachineFacade.save(stateMachine, userId);
        container.nextInitField()
                .ifPresentOrElse(
                        nextInitField -> dialogService.askToSetInitField(userId, nextInitField),
                        () -> startGame(userId)
                );
    }

    @Override
    public void setCurrentInitFieldValues(CallbackQuery callbackQuery) {
        Long userId = callbackQuery.getFrom().getId();
        StateMachine<QuizUserState, QuizUserEvent> stateMachine = stateMachineFacade.getStateMachine(userId);
        InitFieldsContainer container = getInitFieldsContainer(stateMachine);
        InitFieldValueHolder initFieldValueHolder = getInitFieldValueHolder(container);
        List<String> values = dialogService.getSelectedValues(callbackQuery.getMessage().getReplyMarkup());
        valueValidator.validate(initFieldValueHolder.getInitField(), values);
        initFieldValueHolder.setValue(values);
        stateMachineFacade.save(stateMachine, userId);
        dialogService.confirmInitField(callbackQuery, initFieldValueHolder.getInitField());
        container.nextInitField()
                .ifPresentOrElse(
                        nextInitField -> dialogService.askToSetInitField(userId, nextInitField),
                        () -> startGame(userId)
                );
    }

    @Override
    public void skipCurrentInitField(CallbackQuery callbackQuery) {
        Long userId = callbackQuery.getFrom().getId();
        StateMachine<QuizUserState, QuizUserEvent> stateMachine = stateMachineFacade.getStateMachine(userId);
        InitFieldsContainer initFieldsContainer = getInitFieldsContainer(stateMachine);
        InitFieldValueHolder initFieldValueHolder = getInitFieldValueHolder(initFieldsContainer);
        InitField<?> initField = initFieldValueHolder.getInitField();
        if (initField.required()) {
            throw new RequiredInitFieldException(initField.name());
        }
        initFieldValueHolder.setSkipped(true);
        stateMachineFacade.save(stateMachine, userId);
        dialogService.confirmSkipping(callbackQuery, initField);
        initFieldsContainer.nextInitField()
                .ifPresentOrElse(
                        nextInitField -> dialogService.askToSetInitField(userId, nextInitField),
                        () -> startGame(userId)
                );
    }

    @Override
    public void skipCurrentInitField(Message message) {
        Long userId = message.getFrom().getId();
        StateMachine<QuizUserState, QuizUserEvent> stateMachine = stateMachineFacade.getStateMachine(userId);
        InitFieldsContainer initFieldsContainer = getInitFieldsContainer(stateMachine);
        InitFieldValueHolder initFieldValueHolder = getInitFieldValueHolder(initFieldsContainer);
        InitField<?> initField = initFieldValueHolder.getInitField();
        if (initField.required()) {
            throw new RequiredInitFieldException(initField.name());
        }
        initFieldValueHolder.setSkipped(true);
        stateMachineFacade.save(stateMachine, userId);
        dialogService.confirmSkipping(message, initField);
        initFieldsContainer.nextInitField()
                .ifPresentOrElse(
                        nextInitField -> dialogService.askToSetInitField(userId, nextInitField),
                        () -> startGame(userId)
                );
    }

    private String extractSelectValue(InitField<SelectFormat> initField, String messageText) {
        return TelegramMessageUtils.findOptionByDescription(initField.conf(), messageText)
                .map(SelectOption::id)
                .orElseThrow(() -> new NotSelectedOptionException(initField.name()));
    }

    private InitFieldsContainer getInitFieldsContainer(StateMachine<QuizUserState, QuizUserEvent> stateMachine) {
        return stateMachineFacade
                .getInitFieldsContainer(stateMachine)
                .orElseThrow(InitFieldsContainerIsAbsentException::new);
    }

    private InitFieldValueHolder getInitFieldValueHolder(InitFieldsContainer container) {
        return container
                .getCurrentInitFieldValueHolder()
                .orElseThrow(CurrentInitFieldValueHolderIsAbsent::new);
    }

    private void initGame(Long userId,
                          String selectedSourceId,
                          StateMachine<QuizUserState, QuizUserEvent> stateMachine) {
        List<InitField<?>> fields = apiService.getInitFields(selectedSourceId);
        InitFieldsContainer initFieldsContainer = new InitFieldsContainer(fields);
        stateMachineFacade.setInitFieldsContainer(stateMachine, initFieldsContainer);
        stateMachine.sendEvent(QuizUserEvent.FILL_INIT_DATA);
        stateMachineFacade.save(stateMachine, userId);
        initFieldsContainer.nextInitField().ifPresentOrElse(
                initField -> dialogService.askToSetInitField(userId, initField),
                () -> startGame(userId)
        );
    }

    private void startGame(Long userId) {
        log.info("Start to play a game for user {}", userId);
    }

    @SuppressWarnings("unchecked")
    private Optional<QuizSourceDto> findSourceByName(String sourceName, StateMachine<QuizUserState, ?> stateMachine) {
        try {
            List<QuizSourceDto> sourcesList = (List<QuizSourceDto>) stateMachine.getExtendedState()
                    .getVariables().get("sourcesList");
            return sourcesList.stream()
                    .filter(source -> Objects.equals(source.name(), sourceName))
                    .findFirst();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }

    }

}
