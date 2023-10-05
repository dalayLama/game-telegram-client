package org.quizstorage.components.satemachine;

import org.springframework.statemachine.StateMachinePersist;

public interface QuizStateMachinePersist extends StateMachinePersist<QuizUserState, QuizUserEvent, Long> {


}
