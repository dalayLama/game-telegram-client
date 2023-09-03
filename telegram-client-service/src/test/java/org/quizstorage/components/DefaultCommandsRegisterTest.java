package org.quizstorage.components;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quizstorage.components.telegram.Command;
import org.quizstorage.components.common.LocaleProvider;
import org.quizstorage.components.telegram.DefaultCommandsRegister;
import org.quizstorage.components.telegram.TelegramBotFacade;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

@ExtendWith(MockitoExtension.class)
class DefaultCommandsRegisterTest {

    @Mock
    private TelegramBotFacade botFacade;

    @Mock
    private MessageSource messageSource;

    @Mock
    private LocaleProvider localeProvider;

    @InjectMocks
    private DefaultCommandsRegister register;

    @Test
    void shouldRegisterShownInMenuCommands() {
        Locale expectedLocale = Locale.getDefault();
        given(localeProvider.getLocale()).willReturn(expectedLocale);
        List<BotCommand> expectedCommands = Arrays.stream(Command.values())
                .filter(Command::isShowInMenu)
                .map(b -> {
                    given(messageSource.getMessage(b.getDescriptionCode(), new Object[]{}, expectedLocale))
                            .willReturn(b.getDescriptionCode());
                    return new BotCommand(b.getCommand(), b.getDescriptionCode());
                })
                .toList();

        given(botFacade.registerCommands(expectedCommands)).willReturn(true);

        register.register();

        assertThat(register.isRegistered()).isTrue();
        then(botFacade).should(only()).registerCommands(expectedCommands);
    }

}