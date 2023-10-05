package org.quizstorage.components.telegram;

import lombok.Builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

@Builder
public record SelectKeyboardConfig<T>(
        List<T> objects,
        Function<T, String> textExtractor,
        int rowSize,
        boolean skippable,
        Locale locale
) {

    public SelectKeyboardConfig(Collection<? extends T> objects, Function<T, String> textExtractor, int rowSize, boolean skippable, Locale locale) {
        this(new ArrayList<>(objects), textExtractor, rowSize, skippable, locale);
    }
}
