package org.quizstorage.exceptions;

import org.quizstorage.components.common.ErrorMessage;

public class SourceNotFoundException extends QuizTelegramClientException {

    private static final String MESSAGE_TEMPLATE = "Sources \"%s\" doesn't exist";

    private final String sourceName;

    public SourceNotFoundException(String sourceName) {
        super(MESSAGE_TEMPLATE.formatted(sourceName));
        this.sourceName = sourceName;
    }

    @Override
    public String[] getCodes() {
        return ErrorMessage.SOURCE_NOT_FOUND.getCodes();
    }

    @Override
    public Object[] getArguments() {
        return new Object[] {sourceName};
    }
}
