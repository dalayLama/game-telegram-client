package org.quizstorage.components.telegram;

import org.quizstorage.generator.dto.InitField;
import org.quizstorage.generator.dto.QuizSourceDto;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.io.Serializable;
import java.util.Collection;

public interface BotApiMethodGenerator {

    BotApiMethod<? extends Serializable> selectSourceKeyboard(Long userId,
                                                              String text,
                                                              Collection<? extends QuizSourceDto> sources);

    BotApiMethod<? extends Serializable> selectInitFieldKeyboard(Long userId, InitField<?> initField);
}
