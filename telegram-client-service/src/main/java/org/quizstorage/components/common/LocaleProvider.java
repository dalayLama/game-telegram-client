package org.quizstorage.components.common;

import java.util.Locale;

public interface LocaleProvider {

    String getDefaultLanguageCode();

    Locale getDefaultLocale();

    Locale getLocaleByLanguageCode(String languageCode);

    Locale getLocaleByUserId(Long userId);

    void updateUserLocale(Long userId, String languageCode);
}
