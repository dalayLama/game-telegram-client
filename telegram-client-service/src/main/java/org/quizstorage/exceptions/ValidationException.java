package org.quizstorage.exceptions;

import lombok.Getter;

@Getter
public abstract class ValidationException extends QuizTelegramClientException {

    private static final String MESSAGE_TEMPLATE = "Validation error of field \"%s\"";

    private final String fieldName;

    public ValidationException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }

    public ValidationException(String fieldName) {
        super(MESSAGE_TEMPLATE.formatted(fieldName));
        this.fieldName = fieldName;
    }

    public ValidationException(Throwable cause, String fieldName) {
        super(MESSAGE_TEMPLATE.formatted(fieldName), cause);
        this.fieldName = fieldName;
    }

    @Override
    public Object[] getArguments() {
        return new Object[] {fieldName};
    }
}
