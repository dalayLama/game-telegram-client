package org.quizstorage.exceptions;

import org.quizstorage.components.common.ErrorMessage;

public class NotSelectedOptionException extends ValidationException {
    public NotSelectedOptionException(String fieldName) {
        super(fieldName);
    }

    @Override
    public String[] getCodes() {
        return ErrorMessage.NOT_SELECTED_OPTION.getCodes();
    }
}
