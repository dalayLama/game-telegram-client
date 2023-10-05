package org.quizstorage.components.common.initfield;

import org.quizstorage.exceptions.NotMultiValueFormatException;
import org.quizstorage.exceptions.NotSelectedOptionException;
import org.quizstorage.exceptions.RequiredInitFieldException;
import org.quizstorage.generator.dto.FieldType;
import org.quizstorage.generator.dto.InitField;
import org.quizstorage.generator.dto.SelectFormat;
import org.quizstorage.generator.dto.SelectOption;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SelectInitFieldValidator extends AbstractTypedInitFieldValidator implements InitFieldValueValidator {

    @Override
    public void validate(InitField<?> initField, String value) {
        validate(initField, Collections.singleton(value));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void validate(InitField<?> initField, Collection<String> values) {
        if (initField.required() && values.isEmpty()) {
            throw new RequiredInitFieldException(initField.name());
        }
        innerValidate((InitField<SelectFormat>) initField, values);
    }

    private void innerValidate(InitField<SelectFormat> initField, Collection<String> values) {
        SelectFormat conf = initField.conf();
        if (conf == null) {
            return;
        }
        if (!conf.multiValue() && values.size() > 1) {
            throw new NotMultiValueFormatException(initField.name());
        }
        Set<String> possibleValues = conf.selectOptions().stream()
                .map(SelectOption::id)
                .collect(Collectors.toSet());
        if (!possibleValues.containsAll(values)) {
            throw new NotSelectedOptionException(initField.name());
        }
    }

    @Override
    public FieldType supportedType() {
        return FieldType.SELECT;
    }

}
