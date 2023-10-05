package org.quizstorage.exceptions;

import org.quizstorage.components.common.ErrorMessage;
import org.quizstorage.components.satemachine.QuizUserState;

public class NotFoundStateHandlerException extends QuizTelegramClientException {

    private static final String MESSAGE_TEMPLATE = "A handler for state \"%s\" hasn't been found";

    private final QuizUserState state;

    public NotFoundStateHandlerException(QuizUserState state) {
        super(MESSAGE_TEMPLATE.formatted(state));
        this.state = state;
    }

    @Override
    public String[] getCodes() {
        return ErrorMessage.STATE_HANDLER_NOT_FOUND.getCodes();
    }

    @Override
    public Object[] getArguments() {
        return new Object[] {state.name()};
    }
}
