package org.quizstorage.components.telegram;

import lombok.Builder;

import java.util.Collection;
import java.util.Locale;
import java.util.function.Function;

@Builder
public record SelectKeyboardConfig<T>(
        Collection<T> objects,
        Function<T, String> textExtractor,
        int rowSize,
        boolean skippable,
        Locale locale
) {
}
