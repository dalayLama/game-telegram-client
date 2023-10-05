package org.quizstorage.exceptions;

import org.quizstorage.components.common.ErrorMessage;

public class NumberFormatException extends ValidationException {
    public NumberFormatException(String fieldName) {
        super(fieldName);
    }

    public NumberFormatException(Throwable cause, String fieldName) {
        super(cause, fieldName);
    }

    @Override
    public String[] getCodes() {
        return ErrorMessage.NUMBER_FORMAT.getCodes();
    }
}
