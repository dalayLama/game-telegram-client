package org.quizstorage.components.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RequiredArgsConstructor
@Slf4j
public class SimpleLocaleProvider implements LocaleProvider {

    private final Locale locale;

    private final ConcurrentMap<Long, Locale> userLocales = new ConcurrentHashMap<>();

    @Override
    public String getDefaultLanguageCode() {
        return getDefaultLocale().getLanguage();
    }

    @Override
    public Locale getDefaultLocale() {
        return locale;
    }

    @Override
    public Locale getLocaleByLanguageCode(String languageCode) {
        return new Locale(languageCode);
    }

    @Override
    public Locale getLocaleByUserId(Long userId) {
        Locale userLocale = userLocales.get(userId);
        if (userLocale == null) {
            log.warn("Locale for user doe not exist, using default");
            return locale;
        }
        return userLocale;
    }

    @Override
    public void updateUserLocale(Long userId, String languageCode) {
        userLocales.put(userId, new Locale(languageCode));
    }

}
