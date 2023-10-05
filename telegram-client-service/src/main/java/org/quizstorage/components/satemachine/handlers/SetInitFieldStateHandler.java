package org.quizstorage.components.satemachine.handlers;

import org.quizstorage.components.satemachine.QuizUserEvent;
import org.quizstorage.components.satemachine.QuizUserState;
import org.quizstorage.components.telegram.DialogService;
import org.quizstorage.components.telegram.event.events.CallbackQueryEvent;
import org.quizstorage.components.telegram.event.events.SimpleMessageEvent;
import org.quizstorage.services.GameService;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class SetInitFieldStateHandler implements StateHandler {

    private final GameService gameService;

    private final DialogService dialogService;

    public SetInitFieldStateHandler(GameService gameService,
                                    DialogService dialogService) {
        this.gameService = gameService;
        this.dialogService = dialogService;
    }

    @Override
    public void handleMessageEvent(StateMachine<QuizUserState, QuizUserEvent> stateMachine,
                                   SimpleMessageEvent simpleMessageEvent) {
        if (dialogService.isSkipping(simpleMessageEvent.getMessage())) {
            gameService.skipCurrentInitField(simpleMessageEvent.getMessage());
        } else {
            gameService.setCurrentInitFieldValue(simpleMessageEvent.getMessage());
        }
    }

    @Override
    public void handleCallbackQueryEvent(StateMachine<QuizUserState, QuizUserEvent> stateMachine,
                                         CallbackQueryEvent callbackQueryEvent) {
        CallbackQuery callbackQuery = callbackQueryEvent.getCallbackQuery();
        if (dialogService.isSelectConfirmation(callbackQuery)) {
            gameService.setCurrentInitFieldValues(callbackQuery);
        } else if (dialogService.isSkipping(callbackQuery)) {
            gameService.skipCurrentInitField(callbackQuery);
        } else {
            dialogService.updateSelection(callbackQuery);
        }
    }


    @Override
    public QuizUserState handlingState() {
        return QuizUserState.SETTING_INIT_DATA;
    }

}
