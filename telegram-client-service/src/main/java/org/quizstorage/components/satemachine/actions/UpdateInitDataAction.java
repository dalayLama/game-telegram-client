package org.quizstorage.components.satemachine.actions;

import org.quizstorage.components.satemachine.QuizUserEvent;
import org.quizstorage.components.satemachine.QuizUserState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public class UpdateInitDataAction implements Action<QuizUserState, QuizUserEvent> {

    @Override
    public void execute(StateContext<QuizUserState, QuizUserEvent> context) {

    }

}
