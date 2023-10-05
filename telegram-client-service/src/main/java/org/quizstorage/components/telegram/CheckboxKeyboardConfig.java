package org.quizstorage.components.telegram;

import lombok.Builder;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;

@Builder
public record CheckboxKeyboardConfig<T>(
        List<T> objects,
        Function<T, String> idExtractor,
        Function<T, String> textExtractor,
        int rowSize,
        boolean skippable,
        Locale locale
) { }
