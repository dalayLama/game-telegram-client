package org.quizstorage.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.quizstorage.components.common.initfield.InitFieldValueValidator;
import org.quizstorage.components.satemachine.QuizStateMachine;
import org.quizstorage.components.satemachine.QuizStorageStateMachineFacade;
import org.quizstorage.components.satemachine.QuizUserEvent;
import org.quizstorage.components.telegram.DialogService;
import org.quizstorage.exceptions.CurrentInitFieldValueHolderIsAbsent;
import org.quizstorage.exceptions.NotSelectedOptionException;
import org.quizstorage.exceptions.RequiredInitFieldException;
import org.quizstorage.exceptions.SourceNotFoundException;
import org.quizstorage.generator.dto.*;
import org.quizstorage.objects.InitFieldValueHolder;
import org.quizstorage.objects.InitFieldsContainer;
import org.quizstorage.utils.TelegramMessageUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Objects;

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
        if (stateMachine.isComplete()) {
            stateMachine.start();
        }
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
                () -> startGame(userId)
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
                () -> startGame(userId)
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
                () -> startGame(userId)
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
                () -> startGame(userId)
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
        stateMachine.setInitFieldsContainer(initFieldsContainer);
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

}
