package org.quizstorage.components.common;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class DefaultMessageProvider implements MessageProvider {

    private final MessageSource messageSource;

    private final LocaleProvider localeProvider;

    @Override
    public String getLocaledMessageForUser(Long userId, CoddedMessage message, Object... args) {
        Locale locale = localeProvider.getLocaleByUserId(userId);
        return messageSource.getMessage(message.getCode(), args, locale);
    }

    @Override
    public String getLocaledMessage(Locale locale, CoddedMessage coddedMessage, Object... args) {
        return messageSource.getMessage(coddedMessage.getCode(), args, locale);
    }

    @Override
    public String getLocaledMessageForUser(Long userId, MessageSourceResolvable messageSourceResolvable) {
        Locale locale = localeProvider.getLocaleByUserId(userId);
        return messageSource.getMessage(messageSourceResolvable, locale);
    }

    @Override
    public String getLocaledMessage(Locale locale, MessageSourceResolvable messageSourceResolvable) {
        return messageSource.getMessage(messageSourceResolvable, locale);
    }

}
