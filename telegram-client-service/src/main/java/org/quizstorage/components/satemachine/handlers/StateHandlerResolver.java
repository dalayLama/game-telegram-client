package org.quizstorage.components.satemachine.handlers;

import org.quizstorage.components.satemachine.QuizUserState;

import java.util.Optional;

public interface StateHandlerResolver {

    Optional<StateHandler> resolveByState(QuizUserState state);

}
