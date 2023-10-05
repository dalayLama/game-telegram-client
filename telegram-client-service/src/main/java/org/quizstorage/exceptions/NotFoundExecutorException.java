package org.quizstorage.exceptions;

import org.quizstorage.components.common.ErrorMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NotFoundExecutorException extends UpdateEventResolvingException {

    private static final String TEMPLATE_MESSAGE = "Couldn't find an executor for the command \"%s\"";

    private final String command;

    public NotFoundExecutorException(Update update, String command) {
        super(TEMPLATE_MESSAGE.formatted(command), update);
        this.command = command;
    }

    @Override
    public String[] getCodes() {
        return ErrorMessage.EXECUTOR_NOT_FOUND.getCodes();
    }

    @Override
    public Object[] getArguments() {
        return new Object[] {command};
    }
}
