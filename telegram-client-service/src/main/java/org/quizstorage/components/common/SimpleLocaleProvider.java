package org.quizstorage.components.common;

import lombok.RequiredArgsConstructor;

import java.util.Locale;

@RequiredArgsConstructor
public class SimpleLocaleProvider implements LocaleProvider {

    private final Locale locale;

    @Override
    public String getLanguageCode() {
        return getLocale().getLanguage();
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

}
