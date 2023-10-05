package org.quizstorage.components.common;

import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;

@Getter
public enum ErrorMessage implements CoddedMessage {

    UNKNOWN_ERROR("unknown_error"),
    INTERNAL_ERROR("internal_error"),
    RESOLVING_UPDATE_EVENT("resolving_update_event"),
    EXECUTOR_NOT_FOUND("executor.not_found"),
    SOURCE_NOT_FOUND("source.not_found"),
    STATE_HANDLER_NOT_FOUND("state_handler.not_found"),
    CURRENT_INIT_VALUE_HOLDER_IS_ABSENT("value_holder.current.init_fields.absent"),
    INIT_FIELDS_CONTAINER_IS_ABSENT("container.init_field.absent"),
    INIT_FIELD_VALIDATOR_NOT_FOUND_BY_TYPE("type.init_field.validator.not_found"),
    NOT_MULTI_VALUE_INIT_FIELD("validation.not_multi_value"),
    MUST_BE_NUMBER_INIT_FIELD("validation.must_be_number"),
    NOT_SELECTED_OPTION("validation.not_selected_option"),
    NUMBER_FORMAT("validation.number_format"),
    REQUIRED_FIELD("init_field.validation.required");

    private final String[] codes;

    ErrorMessage(String code, String...codes) {
        this.codes = ArrayUtils.insert(0, codes, code);
    }


    @Override
    public String getCode() {
        return getCodes()[0];
    }

}
