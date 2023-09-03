package org.quizstorage.exceptions;

import org.quizstorage.components.telegram.Command;

public class NotFoundExecutorException extends QuizTelegramClientException {

    private static final String TEMPLATE_MESSAGE = "Couldn't find an executor for the command \"%s\"";

    public NotFoundExecutorException(Command command) {
        this(command.getCommand());
    }

    public NotFoundExecutorException(String command) {
        super(TEMPLATE_MESSAGE.formatted(command));
    }

}
