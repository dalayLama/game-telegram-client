package org.quizstorage.configuration;

import lombok.RequiredArgsConstructor;
import org.quizstorage.components.common.LocaleProvider;
import org.quizstorage.components.common.SimpleLocaleProvider;
import org.quizstorage.configuration.properties.LocaleProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
@RequiredArgsConstructor
public class LocaleConfiguration {

    private final LocaleProperties localeProperties;

    @Bean
    public LocaleProvider localeProvider() {
        return new SimpleLocaleProvider(new Locale(localeProperties.getLanguage(), localeProperties.getCountry()));
    }

}
