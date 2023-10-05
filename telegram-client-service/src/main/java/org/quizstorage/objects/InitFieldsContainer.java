package org.quizstorage.objects;

import lombok.Synchronized;
import org.quizstorage.generator.dto.InitField;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class InitFieldsContainer {

    private final List<InitFieldValueHolder> valueHolders;

    private InitFieldValueHolder currentValueHolder;

    public InitFieldsContainer(Collection<? extends InitField<?>> initFields) {
        valueHolders = initFields.stream()
                .map(InitFieldValueHolder::new)
                .toList();
    }

    public Optional<InitFieldValueHolder> getValueHolder(String fieldName) {
        return valueHolders.stream()
                .filter(setter -> setter.getInitField().name().equals(fieldName))
                .findFirst();
    }

    @Synchronized
    public Optional<InitField<?>> nextInitField() {
        Optional<InitFieldValueHolder> first = valueHolders.stream()
                .filter(valueHolder -> !valueHolder.isSkipped())
                .filter(valueHolder -> !valueHolder.hasValue())
                .findFirst();
        currentValueHolder = first.orElse(null);
        return first.map(InitFieldValueHolder::getInitField);
    }

    public Optional<InitFieldValueHolder> getCurrentInitFieldValueHolder() {
        return Optional.ofNullable(currentValueHolder);
    }
}
