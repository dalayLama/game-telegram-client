package org.quizstorage.components.common;

import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;

@Getter
public enum InfoMessage implements CoddedMessage {

    SOURCE_SELECTED("source.selected"),
    OPTION_SELECTED("option.selected"),
    INIT_FIELD_VALUE_ACCEPTED("init_field.value.accepted"),
    SELECT_SOURCE("source.select"),
    FILL_INIT_FIELD("init_field.fill"),
    SELECT_INIT_FIELD("init_field.select"),
    NUMBER_FORMAT("number.format"),
    INIT_FIELD_SKIPPED("init_field.skipped");


    private final String[] codes;

    InfoMessage(String code, String... codes) {
        this.codes = ArrayUtils.insert(0, codes, code);
    }

    @Override
    public String getCode() {
        return getCodes()[0];
    }
}
