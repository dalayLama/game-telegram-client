package org.quizstorage.exceptions;

import org.quizstorage.components.common.ErrorMessage;

public class NotNumberException extends ValidationException {

    private static final String MESSAGE_TEMPLATE = "%s is expected to be a number";

    public NotNumberException(String fieldName) {
        super(fieldName, MESSAGE_TEMPLATE.formatted(fieldName));
    }

    @Override
    public String[] getCodes() {
        return ErrorMessage.MUST_BE_NUMBER_INIT_FIELD.getCodes();
    }

}
