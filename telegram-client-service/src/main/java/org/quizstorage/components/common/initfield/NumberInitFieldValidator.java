package org.quizstorage.components.common.initfield;

import org.apache.commons.lang3.math.NumberUtils;
import org.quizstorage.exceptions.NotNumberException;
import org.quizstorage.exceptions.NumberFormatException;
import org.quizstorage.generator.dto.FieldType;
import org.quizstorage.generator.dto.InitField;
import org.quizstorage.generator.dto.NumberFormat;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public class NumberInitFieldValidator extends AbstractTypedInitFieldValidator implements TypedInitFieldValidator {

    @Override
    public void validate(InitField<?> initField, String value) {
        if (!NumberUtils.isDigits(value)) {
            throw new NotNumberException(initField.name());
        }
        int number = NumberUtils.toInt(value);
        Optional.ofNullable(initField.conf())
                .map(conf -> (NumberFormat) conf)
                .ifPresent(format -> checkFormat(initField, number, format));
    }

    @Override
    public void validate(InitField<?> initField, Collection<String> values) {

    }

    @Override
    public FieldType supportedType() {
        return FieldType.NUMBER;
    }

    private void checkFormat(InitField<?> initField, int value, NumberFormat format) {
        if (value < format.minValue() || value > format.maxValue()) {
            throw new NumberFormatException(initField.name());
        }

    }

}
