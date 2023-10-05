package org.quizstorage.components.satemachine.handlers;

import org.quizstorage.components.satemachine.QuizUserEvent;
import org.quizstorage.components.satemachine.QuizUserState;
import org.quizstorage.components.telegram.event.events.CallbackQueryEvent;
import org.quizstorage.components.telegram.event.events.SimpleMessageEvent;
import org.springframework.statemachine.StateMachine;

public interface StateHandler {

    void handleMessageEvent(StateMachine<QuizUserState, QuizUserEvent> stateMachine,
                            SimpleMessageEvent simpleMessageEvent);

    void handleCallbackQueryEvent(StateMachine<QuizUserState, QuizUserEvent> stateMachine,
                                  CallbackQueryEvent callbackQueryEvent);

    QuizUserState handlingState();

}
