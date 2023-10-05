package org.quizstorage.objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.quizstorage.generator.dto.InitField;

@RequiredArgsConstructor
@Getter
public class InitFieldValueHolder {

    private final InitField<?> initField;

    private Object value;

    private boolean skipped;

    public void setValue(Object value) {
        this.value = value;
        this.skipped = false;
    }

    public void setSkipped(boolean skipped) {
        this.skipped = skipped;
        this.value = null;
    }

    public boolean hasValue() {
        return ObjectUtils.isNotEmpty(value);
    }

}
