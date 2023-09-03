package org.quizstorage.components;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quizstorage.components.common.LocaleProvider;
import org.quizstorage.components.telegram.DefaultTelegramBotFacade;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

@ExtendWith(MockitoExtension.class)
class DefaultTelegramBotFacadeTest {

    @Mock
    private TelegramLongPollingBot bot;

    @Mock
    private LocaleProvider localeProvider;

    @InjectMocks
    private DefaultTelegramBotFacade botFacade;

    @Test
    void shouldRegisterCommands() throws TelegramApiException {
        List<BotCommand> commands = List.of(new BotCommand("commands", "description"));
        String languageCode = "en";


        SetMyCommands expectedCommands = new SetMyCommands(commands, new BotCommandScopeDefault(), languageCode);

        given(localeProvider.getLanguageCode()).willReturn(languageCode);
        given(bot.execute(expectedCommands)).willReturn(true);

        boolean result = botFacade.registerCommands(commands);

        assertThat(result).isTrue();
        then(localeProvider).should(only()).getLanguageCode();
        then(bot).should(only()).execute(expectedCommands);
    }

}