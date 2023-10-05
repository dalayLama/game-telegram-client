package org.quizstorage.components.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quizstorage.components.common.LocaleProvider;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultCommandsRegister implements CommandsRegister {

    private final TelegramBotFacade botFacade;

    private final MessageSource commandsDescriptions;

    private final LocaleProvider localeProvider;

    private boolean registered = false;

    @Override
    @EventListener(ApplicationReadyEvent.class)
    public void register() {
        if (!isRegistered()) {
            List<BotCommand> botCommands = getBotCommands();
            registered = botFacade.registerCommands(botCommands);
            if (registered) {
                logRegisteredCommands(botCommands);
            }
        }
    }

    @Override
    public boolean isRegistered() {
        return registered;
    }

    private List<BotCommand> getBotCommands() {
        return Arrays.stream(Command.values())
                .filter(Command::isShowInMenu)
                .map(this::toBotCommand)
                .toList();
    }

    private BotCommand toBotCommand(Command command) {
        Locale locale = localeProvider.getDefaultLocale();
        String description = commandsDescriptions.getMessage(command.getDescriptionCode(), new Object[]{}, locale);
        return new BotCommand(command.getName(), description);
    }

    private void logRegisteredCommands(Collection<? extends BotCommand> botCommands) {
        String stringCommands = botCommands.stream()
                .map(bc -> "%s:%s".formatted(bc.getCommand(), bc.getDescription()))
                .collect(Collectors.joining("\n"));
        log.info("Commands have been registered:\n{}", stringCommands);
    }

}
