package org.quizstorage.components.common.initfield;

import org.quizstorage.generator.dto.InitField;

import java.util.Collection;

public interface InitFieldValueValidator {

    void validate(InitField<?> initField, String value);

    void validate(InitField<?> initField, Collection<String> values);

}
