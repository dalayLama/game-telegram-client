package org.quizstorage.configuration;

import org.quizstorage.components.common.LocaleProvider;
import org.quizstorage.components.common.MessageProvider;
import org.quizstorage.components.telegram.*;
import org.quizstorage.generator.dto.FieldType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class InitFieldBotApiMethodFactoryConfiguration {

    @Bean
    public Map<FieldType, InitFieldBotApiMethodFactory> initFieldBotApiMethodFactoryMap(
            KeyboardManager keyboardManager,
            MessageProvider messageProvider,
            NumberInitFieldFormatTemplate numberInitFieldFormatTemplate,
            LocaleProvider localeProvider) {
        return Map.of(
                FieldType.SELECT, new SelectInitFieldBotApiMethodFactory(keyboardManager, messageProvider, localeProvider),
                FieldType.NUMBER, new NumberInitFieldBotApiMethodFactory(messageProvider, numberInitFieldFormatTemplate, keyboardManager, localeProvider)
        );
    }

}
