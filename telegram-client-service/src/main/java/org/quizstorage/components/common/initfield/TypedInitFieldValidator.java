package org.quizstorage.components.common.initfield;

import org.quizstorage.generator.dto.FieldType;

public interface TypedInitFieldValidator extends InitFieldValueValidator {

    FieldType supportedType();

}
