package org.quizstorage.utils;

import lombok.experimental.UtilityClass;
import org.quizstorage.generator.dto.SelectFormat;
import org.quizstorage.generator.dto.SelectOption;

import java.util.Collection;
import java.util.Optional;

@UtilityClass
public class TelegramMessageUtils {

    public static Optional<SelectOption> findOptionByDescription(SelectFormat selectFormat, String description) {
        return Optional.ofNullable(selectFormat)
                .stream()
                .map(SelectFormat::selectOptions)
                .flatMap(Collection::stream)
                .filter(option -> option.description().equals(description))
                .findFirst();

    }

}
