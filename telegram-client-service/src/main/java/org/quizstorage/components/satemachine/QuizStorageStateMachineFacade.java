package org.quizstorage.components.satemachine;

public interface QuizStorageStateMachineFacade {

    QuizStateMachine getStateMachine();

    QuizStateMachine getStateMachine(Long userId);

    void save(QuizStateMachine stateMachine, Long userId);

}
