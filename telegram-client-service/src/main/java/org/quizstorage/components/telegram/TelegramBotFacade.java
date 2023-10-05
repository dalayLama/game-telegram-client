package org.quizstorage.components.telegram;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.io.Serializable;
import java.util.Collection;

public interface TelegramBotFacade {

    boolean registerCommands(Collection<? extends BotCommand> commands);

    void sendMessage(Long userId, String text);

    <T extends Serializable> T execute(BotApiMethod<T> method);

    <T extends Serializable> T execute(BotApiMethod<T> method, T defaultValue);
}
