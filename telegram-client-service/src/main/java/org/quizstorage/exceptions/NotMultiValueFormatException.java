package org.quizstorage.exceptions;

import org.quizstorage.components.common.ErrorMessage;

public class NotMultiValueFormatException extends ValidationException {

    private static final String MESSAGE_TEMPLATE = "Field \"%s\" can't be multi value";

    public NotMultiValueFormatException(String fieldName) {
        super(fieldName, MESSAGE_TEMPLATE.formatted(fieldName));
    }

    @Override
    public String[] getCodes() {
        return ErrorMessage.NOT_MULTI_VALUE_INIT_FIELD.getCodes();
    }

}
