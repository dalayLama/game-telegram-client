package org.quizstorage.components.telegram;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.Collection;

public interface TelegramBotFacade {

    boolean registerCommands(Collection<? extends BotCommand> commands);

}
