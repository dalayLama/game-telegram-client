package org.quizstorage.components.satemachine;

import lombok.RequiredArgsConstructor;
import org.quizstorage.exceptions.PersistingContextException;
import org.quizstorage.exceptions.RestoringContextException;
import org.quizstorage.objects.InitFieldsContainer;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DefaultQuizStorageStateMachineFacade implements QuizStorageStateMachineFacade {

    private final StateMachineFactory<QuizUserState, QuizUserEvent> stateMachineFactory;

    private final StateMachinePersister<QuizUserState, QuizUserEvent, Long> persister;

    @Override
    public StateMachine<QuizUserState, QuizUserEvent> getStateMachine() {
        return stateMachineFactory.getStateMachine();
    }

    @Override
    public StateMachine<QuizUserState, QuizUserEvent> getStateMachine(Long userId) {
        try {
            StateMachine<QuizUserState, QuizUserEvent> stateMachine = getStateMachine();
            persister.restore(stateMachine, userId);
            return stateMachine;
        } catch (Exception e) {
            throw new RestoringContextException(e);
        }
    }

    @Override
    public void save(StateMachine<QuizUserState, QuizUserEvent> stateMachine, Long userId) {
        try {
            persister.persist(stateMachine, userId);
        } catch (Exception e) {
            throw new PersistingContextException(e);
        }
    }

    @Override
    public <T> Optional<T> getVariable(StateMachine<QuizUserState, QuizUserEvent> stateMachine, String name, Class<T> clazz) {
        return Optional.ofNullable(stateMachine.getExtendedState().get(name, clazz));
    }

    @Override
    public Optional<Object> getVariable(StateMachine<QuizUserState, QuizUserEvent> stateMachine, String name) {
        return Optional.ofNullable(stateMachine.getExtendedState().getVariables().get(name));
    }

    @Override
    public <T> Optional<T> getVariable(Long userId, String name, Class<T> clazz) {
        StateMachine<QuizUserState, QuizUserEvent> stateMachine = getStateMachine(userId);
        return getVariable(stateMachine, name, clazz);
    }

    @Override
    public Optional<Object> getVariable(Long userId, String name) {
        StateMachine<QuizUserState, QuizUserEvent> stateMachine = getStateMachine(userId);
        return getVariable(stateMachine, name);
    }

    @Override
    public <T> void setVariable(Long userId, String name, T value) {
        StateMachine<QuizUserState, QuizUserEvent> stateMachine = getStateMachine(userId);
        stateMachine.getExtendedState().getVariables().put(name, value);
    }

    @Override
    public void setInitFieldsContainer(StateMachine<QuizUserState, QuizUserEvent> stateMachine,
                                       InitFieldsContainer container) {
        stateMachine.getExtendedState().getVariables().put("initFieldsContainer", container);
    }

    @Override
    public Optional<InitFieldsContainer> getInitFieldsContainer(StateMachine<QuizUserState, QuizUserEvent> stateMachine) {
        InitFieldsContainer container = stateMachine.getExtendedState().get("initFieldsContainer", InitFieldsContainer.class);
        return Optional.ofNullable(container);
    }
}
