package org.quizstorage.components.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quizstorage.components.common.LocaleProvider;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultTelegramBotFacade implements TelegramBotFacade {

    private final TelegramLongPollingBot bot;

    private final LocaleProvider localeProvider;

    @Override
    public boolean registerCommands(Collection<? extends BotCommand> commands) {
        List<BotCommand> botCommands = new ArrayList<>(commands);
        BotCommandScopeDefault scopeDefault = new BotCommandScopeDefault();
        var setMyCommands = new SetMyCommands(botCommands, scopeDefault, localeProvider.getLanguageCode());
        try {
            return bot.execute(setMyCommands);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

}
