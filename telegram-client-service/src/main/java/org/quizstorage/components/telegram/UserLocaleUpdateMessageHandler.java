package org.quizstorage.components.telegram;

import lombok.RequiredArgsConstructor;
import org.quizstorage.components.common.LocaleProvider;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Primary
@Component
@RequiredArgsConstructor
public class UserLocaleUpdateMessageHandler implements UserMessageHandler {

    private final LocaleProvider localeProvider;

    private final UserMessageHandler delegate;

    @Override
    public void handle(Update update) {
        User from = update.hasMessage()
                ? update.getMessage().getFrom()
                : update.getCallbackQuery().getFrom();
        localeProvider.updateUserLocale(from.getId(), from.getLanguageCode());
        delegate.handle(update);
    }
}
