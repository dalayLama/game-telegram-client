package org.quizstorage.components.common.initfield;

import org.quizstorage.exceptions.InitFieldValidatorNotFoundException;
import org.quizstorage.generator.dto.FieldType;
import org.quizstorage.generator.dto.InitField;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Primary
public class ValidatorsManager implements InitFieldValueValidator {

    private final Map<FieldType, TypedInitFieldValidator> validatorsMap;

    public ValidatorsManager(Collection<? extends TypedInitFieldValidator> validators) {
        validatorsMap = validators.stream()
                .collect(Collectors.toMap(TypedInitFieldValidator::supportedType, Function.identity()));
    }

    @Override
    public void validate(InitField<?> initField, String value) {
        getValidator(initField).validate(initField, value);
    }

    @Override
    public void validate(InitField<?> initField, Collection<String> values) {
        getValidator(initField).validate(initField, values);
    }

    private TypedInitFieldValidator getValidator(InitField<?> initField) {
        return Optional.ofNullable(validatorsMap.get(initField.type()))
                .orElseThrow(() -> new InitFieldValidatorNotFoundException(initField.type()));
    }

}
