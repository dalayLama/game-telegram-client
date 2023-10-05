package org.quizstorage.exceptions;

import org.quizstorage.generator.dto.FieldType;

public class InitFieldBotApiMethodFactoryNotFoundException extends InternalServerException {

    private static final String MESSAGE_TEMPLATE = "Factory for type \"%s\" not found";

    public InitFieldBotApiMethodFactoryNotFoundException(FieldType type) {
        super(MESSAGE_TEMPLATE.formatted(type));
    }
}
