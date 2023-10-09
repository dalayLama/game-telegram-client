package org.quizstorage.components.satemachine;

import lombok.RequiredArgsConstructor;
import org.quizstorage.exceptions.PersistingContextException;
import org.quizstorage.exceptions.RestoringContextException;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultQuizStorageStateMachineFacade implements QuizStorageStateMachineFacade {

    private final StateMachineFactory<QuizUserState, QuizUserEvent> stateMachineFactory;

    private final StateMachinePersister<QuizUserState, QuizUserEvent, Long> persister;

    @Override
    public QuizStateMachine getStateMachine() {
        StateMachine<QuizUserState, QuizUserEvent> stateMachine = stateMachineFactory.getStateMachine();
        return new QuizStateMachine(stateMachine);
    }

    @Override
    public QuizStateMachine getStateMachine(Long userId) {
        try {
            StateMachine<QuizUserState, QuizUserEvent> stateMachine = stateMachineFactory.getStateMachine();
            persister.restore(stateMachine, userId);
            return new QuizStateMachine(stateMachine);
        } catch (Exception e) {
            throw new RestoringContextException(e);
        }
    }

    @Override
    public void save(QuizStateMachine stateMachine, Long userId) {
        try {
            persister.persist(stateMachine.getDelegate(), userId);
        } catch (Exception e) {
            throw new PersistingContextException(e);
        }
    }

}
