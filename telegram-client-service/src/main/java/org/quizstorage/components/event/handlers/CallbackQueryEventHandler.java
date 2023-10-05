package org.quizstorage.components.event.handlers;

import lombok.RequiredArgsConstructor;
import org.quizstorage.annotations.CatchException;
import org.quizstorage.annotations.WrapUserResolvableException;
import org.quizstorage.components.event.events.CallbackQueryEvent;
import org.quizstorage.components.satemachine.QuizStorageStateMachineFacade;
import org.quizstorage.components.satemachine.QuizUserEvent;
import org.quizstorage.components.satemachine.QuizUserState;
import org.quizstorage.components.satemachine.handlers.StateHandler;
import org.quizstorage.components.satemachine.handlers.StateHandlerResolver;
import org.quizstorage.exceptions.NotFoundStateHandlerException;
import org.springframework.context.event.EventListener;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CallbackQueryEventHandler {

    private final StateHandlerResolver stateHandlerResolver;

    private final QuizStorageStateMachineFacade stateMachineFacade;

    @EventListener(CallbackQueryEvent.class)
    @WrapUserResolvableException
    @CatchException
    public void handleCallback(CallbackQueryEvent event) {
        Long userId = event.getCallbackQuery().getFrom().getId();
        StateMachine<QuizUserState, QuizUserEvent> stateMachine = stateMachineFacade
                .getStateMachine(userId);
        StateHandler stateHandler = stateHandlerResolver.resolveByState(stateMachine.getState().getId())
                .orElseThrow(() -> new NotFoundStateHandlerException(stateMachine.getState().getId()));
        stateHandler.handleCallbackQueryEvent(stateMachine, event);
    }

}
