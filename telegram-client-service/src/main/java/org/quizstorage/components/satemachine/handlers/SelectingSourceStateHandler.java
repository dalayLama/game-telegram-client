package org.quizstorage.components.satemachine.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quizstorage.components.satemachine.QuizUserEvent;
import org.quizstorage.components.satemachine.QuizUserState;
import org.quizstorage.components.telegram.event.events.CallbackQueryEvent;
import org.quizstorage.components.telegram.event.events.SimpleMessageEvent;
import org.quizstorage.services.GameService;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SelectingSourceStateHandler implements StateHandler {

    private final GameService gameService;

    @Override
    public void handleMessageEvent(StateMachine<QuizUserState, QuizUserEvent> stateMachine,
                                   SimpleMessageEvent simpleMessageEvent) {
        gameService.selectSourceByName(simpleMessageEvent.getMessage());
    }

    @Override
    public void handleCallbackQueryEvent(StateMachine<QuizUserState, QuizUserEvent> stateMachine,
                                         CallbackQueryEvent callbackQueryEvent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public QuizUserState handlingState() {
        return QuizUserState.SELECTING_SOURCE;
    }

}
