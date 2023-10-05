package org.quizstorage.exceptions;

import org.quizstorage.components.common.ErrorMessage;
import org.quizstorage.generator.dto.FieldType;

public class InitFieldValidatorNotFoundException extends QuizTelegramClientException {

    private static final String NOT_FOUND_BY_TYPE_TEMPLATE = "Validator for type \"%s\" hasn't been found";

    private final FieldType fieldType;

    private final ErrorMessage errorMessage;

    private final Object[] arguments;

    public InitFieldValidatorNotFoundException(FieldType type) {
        super(NOT_FOUND_BY_TYPE_TEMPLATE.formatted(type.name()));
        this.fieldType = type;
        errorMessage = ErrorMessage.INIT_FIELD_VALIDATOR_NOT_FOUND_BY_TYPE;
        this.arguments = new Object[] {type.name()};
    }

    @Override
    public String[] getCodes() {
        return new String[] {errorMessage.getCode()};
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }
}
