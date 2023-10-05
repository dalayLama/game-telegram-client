package org.quizstorage.components.event.handlers;

import lombok.RequiredArgsConstructor;
import org.quizstorage.annotations.CatchException;
import org.quizstorage.annotations.WrapUserResolvableException;
import org.quizstorage.components.satemachine.QuizStorageStateMachineFacade;
import org.quizstorage.components.satemachine.QuizUserEvent;
import org.quizstorage.components.satemachine.QuizUserState;
import org.quizstorage.components.satemachine.handlers.StateHandler;
import org.quizstorage.components.satemachine.handlers.StateHandlerResolver;
import org.quizstorage.components.event.events.SimpleMessageEvent;
import org.quizstorage.exceptions.NotFoundStateHandlerException;
import org.springframework.context.event.EventListener;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SimpleMessageEventHandler {

    private final StateHandlerResolver stateHandlerResolver;

    private final QuizStorageStateMachineFacade stateMachineFacade;

    @EventListener(SimpleMessageEvent.class)
    @WrapUserResolvableException
    @CatchException
    public void handleSimpleMessage(SimpleMessageEvent event) {
        Long userId = event.getMessage().getFrom().getId();
        StateMachine<QuizUserState, QuizUserEvent> stateMachine = stateMachineFacade
                .getStateMachine(userId);
        StateHandler stateHandler = stateHandlerResolver.resolveByState(stateMachine.getState().getId())
                .orElseThrow(() -> new NotFoundStateHandlerException(stateMachine.getState().getId()));
        stateHandler.handleMessageEvent(stateMachine, event);
    }

}
