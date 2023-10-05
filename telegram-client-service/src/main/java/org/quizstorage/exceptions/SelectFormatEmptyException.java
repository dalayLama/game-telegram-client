package org.quizstorage.exceptions;

public class SelectFormatEmptyException extends InternalServerException {

    private final static String MESSAGE_TEMPLATE = "Format of the select field \"%s\" doesn't have options";

    public SelectFormatEmptyException(String fieldName) {
        super(MESSAGE_TEMPLATE.formatted(fieldName));
    }
}
