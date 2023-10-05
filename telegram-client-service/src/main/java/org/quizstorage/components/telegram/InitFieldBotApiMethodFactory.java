package org.quizstorage.components.telegram;

import org.quizstorage.generator.dto.InitField;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.io.Serializable;

public interface InitFieldBotApiMethodFactory {

    BotApiMethod<? extends Serializable> create(Long userId, InitField<?> initField);

}
