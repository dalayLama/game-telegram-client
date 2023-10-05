package org.quizstorage.exceptions;

import org.quizstorage.components.common.ErrorMessage;

public class RequiredInitFieldException extends ValidationException {

    public static final String MESSAGE_TEMPLATE = "Field \"%s\" is required";

    public RequiredInitFieldException(String fieldName) {
        super(fieldName, MESSAGE_TEMPLATE.formatted(fieldName));
    }

    @Override
    public String[] getCodes() {
        return ErrorMessage.REQUIRED_FIELD.getCodes();
    }
}
