package org.quizstorage.components.satemachine.handlers;

import lombok.RequiredArgsConstructor;
import org.quizstorage.components.event.events.CallbackQueryEvent;
import org.quizstorage.components.event.events.SimpleMessageEvent;
import org.quizstorage.components.satemachine.QuizUserEvent;
import org.quizstorage.components.satemachine.QuizUserState;
import org.quizstorage.components.telegram.DialogService;
import org.quizstorage.services.GameService;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class PlayGameStateHandler implements StateHandler {

    private final GameService gameService;

    private final DialogService dialogService;

    @Override
    public void handleMessageEvent(StateMachine<QuizUserState, QuizUserEvent> stateMachine,
                                   SimpleMessageEvent simpleMessageEvent) {
        gameService.acceptAnswer(simpleMessageEvent.getMessage());
    }

    @Override
    public void handleCallbackQueryEvent(StateMachine<QuizUserState, QuizUserEvent> stateMachine,
                                         CallbackQueryEvent callbackQueryEvent) {
        CallbackQuery callbackQuery = callbackQueryEvent.getCallbackQuery();
        if (dialogService.isSelectConfirmation(callbackQuery)) {
            gameService.acceptAnswers(callbackQuery);
        } else {
            dialogService.updateSelection(callbackQuery);
        }
    }

    @Override
    public QuizUserState handlingState() {
        return QuizUserState.PLAY_GAME;
    }

}
