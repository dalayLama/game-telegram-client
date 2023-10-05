package org.quizstorage.components.telegram;

import lombok.RequiredArgsConstructor;
import org.quizstorage.generator.dto.InitField;
import org.quizstorage.generator.dto.QuizSourceDto;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class DefaultBotApiMethodGenerator implements BotApiMethodGenerator {

    private final KeyboardManager keyboardManager;

    private final InitFieldBotApiMethodFactory initFieldBotApiMethodFactory;

    @Override
    public BotApiMethod<? extends Serializable> selectSourceKeyboard(Long userId,
                                                                     String text,
                                                                     Collection<? extends QuizSourceDto> sources) {
        SelectKeyboardConfig<QuizSourceDto> config = SelectKeyboardConfig.<QuizSourceDto>builder()
                .objects(new ArrayList<>(sources))
                .textExtractor(QuizSourceDto::name)
                .rowSize(1)
                .skippable(false)
                .build();
        ReplyKeyboardMarkup keyboard = keyboardManager.selectKeyboard(config);
        return SendMessage.builder()
                .chatId(userId.toString())
                .text(text)
                .replyMarkup(keyboard)
                .build();
    }

    @Override
    public BotApiMethod<? extends Serializable> selectInitFieldKeyboard(Long userId, InitField<?> initField) {
        return initFieldBotApiMethodFactory.create(userId, initField);
    }

}
