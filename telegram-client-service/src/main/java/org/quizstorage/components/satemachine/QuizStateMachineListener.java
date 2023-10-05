package org.quizstorage.components.satemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

@Slf4j
public class QuizStateMachineListener extends StateMachineListenerAdapter<QuizUserState, QuizUserEvent> {

    @Override
    public void stateChanged(State<QuizUserState, QuizUserEvent> from, State<QuizUserState, QuizUserEvent> to) {
        log.info("Transitioned from {} to {}", from == null ? "none" : from.getId(), to.getId());
    }

    @Override
    public void stateEntered(State<QuizUserState, QuizUserEvent> state) {
        log.info("Entered to {}", state);
    }

    @Override
    public void stateExited(State<QuizUserState, QuizUserEvent> state) {
        log.info("Exited to {}", state);
    }

    @Override
    public void eventNotAccepted(Message<QuizUserEvent> event) {
        log.info("Event not accepted {}", event);
    }
}