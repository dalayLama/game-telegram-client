package org.quizstorage.components.telegram;

import lombok.RequiredArgsConstructor;
import org.quizstorage.components.common.InfoMessage;
import org.quizstorage.components.common.LocaleProvider;
import org.quizstorage.components.common.MessageProvider;
import org.quizstorage.exceptions.SelectFormatEmptyException;
import org.quizstorage.generator.dto.InitField;
import org.quizstorage.generator.dto.SelectFormat;
import org.quizstorage.generator.dto.SelectOption;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.io.Serializable;

@RequiredArgsConstructor
@Component
public class SelectInitFieldBotApiMethodFactory implements InitFieldBotApiMethodFactory {

    private final KeyboardManager keyboardManager;

    private final MessageProvider messageProvider;

    private final LocaleProvider localeProvider;

    @Override
    public BotApiMethod<? extends Serializable> create(Long userId, InitField<?> initField) {
        SelectFormat format = (SelectFormat) initField.conf();
        if (format == null || format.selectOptions().isEmpty()) {
            throw new SelectFormatEmptyException(initField.name());
        }
        return format.multiValue()
                ? multiValueSelect(userId, initField, format)
                : oneValueSelect(userId, initField, format);
    }

    private BotApiMethod<? extends Serializable> multiValueSelect(Long userId,
                                                                  InitField<?> initField,
                                                                  SelectFormat format) {
        CheckboxKeyboardConfig<SelectOption> config = CheckboxKeyboardConfig.<SelectOption>builder()
                .objects(format.selectOptions())
                .idExtractor(SelectOption::id)
                .textExtractor(SelectOption::description)
                .rowSize(4)
                .skippable(!initField.required())
                .locale(localeProvider.getLocaleByUserId(userId))
                .build();

        InlineKeyboardMarkup checkboxKeyboard = keyboardManager.checkboxKeyboard(config);
        String text = selectValueMessage(userId, initField);
        return SendMessage.builder()
                .chatId(userId)
                .text(text)
                .replyMarkup(checkboxKeyboard)
                .build();
    }

    private BotApiMethod<? extends Serializable> oneValueSelect(Long userId,
                                                                InitField<?> initField,
                                                                SelectFormat format) {
        SelectKeyboardConfig<SelectOption> config = SelectKeyboardConfig.<SelectOption>builder()
                .objects(format.selectOptions())
                .textExtractor(SelectOption::description)
                .rowSize(1)
                .skippable(!initField.required())
                .locale(localeProvider.getLocaleByUserId(userId))
                .build();
        ReplyKeyboardMarkup keyboard = keyboardManager.selectKeyboard(config);
        String text = selectValueMessage(userId, initField);
        return SendMessage.builder()
                .chatId(userId)
                .text(text)
                .replyMarkup(keyboard)
                .build();
    }

    private String selectValueMessage(Long userId, InitField<?> initField) {
        return messageProvider.getLocaledMessageForUser(
                userId,
                InfoMessage.SELECT_INIT_FIELD,
                initField.name(),
                initField.description()
        );
    }

}
