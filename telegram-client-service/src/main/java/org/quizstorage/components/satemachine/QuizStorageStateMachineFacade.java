package org.quizstorage.components.satemachine;

import org.quizstorage.objects.InitFieldsContainer;
import org.springframework.statemachine.StateMachine;

import java.util.Optional;

public interface QuizStorageStateMachineFacade {

    StateMachine<QuizUserState, QuizUserEvent> getStateMachine();

    StateMachine<QuizUserState, QuizUserEvent> getStateMachine(Long userId);

    void save(StateMachine<QuizUserState, QuizUserEvent> stateMachine, Long userId);

    <T> Optional<T> getVariable(StateMachine<QuizUserState, QuizUserEvent> stateMachine, String name, Class<T> clazz);

    Optional<Object> getVariable(StateMachine<QuizUserState, QuizUserEvent> stateMachine, String name);

    <T> Optional<T> getVariable(Long userId, String name, Class<T> clazz);

    Optional<Object> getVariable(Long userId, String name);

    <T> void setVariable(Long userId, String name, T value);

    void setInitFieldsContainer(StateMachine<QuizUserState, QuizUserEvent> stateMachine,
                                InitFieldsContainer container);

    Optional<InitFieldsContainer> getInitFieldsContainer(StateMachine<QuizUserState, QuizUserEvent> stateMachine);

}
