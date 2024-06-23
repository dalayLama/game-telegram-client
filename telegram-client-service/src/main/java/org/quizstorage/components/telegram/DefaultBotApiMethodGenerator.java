package org.quizstorage.components.telegram;

import lombok.RequiredArgsConstructor;
import org.quizstoradge.director.dto.GameQuestionDto;
import org.quizstorage.components.common.LocaleProvider;
import org.quizstorage.generator.dto.InitField;
import org.quizstorage.generator.dto.QuizSourceDto;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DefaultBotApiMethodGenerator implements BotApiMethodGenerator {

    private final KeyboardManager keyboardManager;

    private final InitFieldBotApiMethodFactory initFieldBotApiMethodFactory;

    private final LocaleProvider localeProvider;

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

    @Override
    public BotApiMethod<? extends Serializable> questionKeyboard(Long userId, GameQuestionDto question) {
        return question.multiplyAnswers()
                ? checkboxQuestionKeyboard(userId, question)
                : selectQuestionKeyboard(userId, question);
    }

    private BotApiMethod<? extends Serializable> selectQuestionKeyboard(Long userId, GameQuestionDto question) {
        Locale locale = localeProvider.getLocaleByUserId(userId);
        SelectKeyboardConfig<String> config = SelectKeyboardConfig.<String>builder()
                .objects(question.answers())
                .textExtractor(Function.identity())
                .locale(locale)
                .rowSize(1)
                .skippable(false)
                .build();
        ReplyKeyboardMarkup keyboard = keyboardManager.selectKeyboard(config);
        return SendMessage.builder()
                .chatId(userId.toString())
                .text(question.question())
                .replyMarkup(keyboard)
                .build();

    }

    private BotApiMethod<? extends Serializable> checkboxQuestionKeyboard(Long userId, GameQuestionDto question) {
        Locale locale = localeProvider.getLocaleByUserId(userId);
        CheckboxKeyboardConfig<String> config = CheckboxKeyboardConfig.<String>builder()
                .objects(question.answers())
                .idExtractor(Function.identity())
                .textExtractor(Function.identity())
                .rowSize(2)
                .skippable(false)
                .locale(locale)
                .build();
        InlineKeyboardMarkup inlineKeyboardMarkup = keyboardManager.checkboxKeyboard(config);
        return SendMessage.builder()
                .chatId(userId.toString())
                .text(question.question())
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }

}
