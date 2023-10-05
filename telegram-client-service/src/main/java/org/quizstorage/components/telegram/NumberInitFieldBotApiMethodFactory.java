package org.quizstorage.components.telegram;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.quizstorage.components.common.InfoMessage;
import org.quizstorage.components.common.LocaleProvider;
import org.quizstorage.components.common.MessageProvider;
import org.quizstorage.generator.dto.InitField;
import org.quizstorage.generator.dto.NumberFormat;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.io.Serializable;

@Component
@RequiredArgsConstructor
public class NumberInitFieldBotApiMethodFactory implements InitFieldBotApiMethodFactory {

    private final MessageProvider messageProvider;

    private final NumberInitFieldFormatTemplate formatTemplate;

    private final KeyboardManager keyboardManager;

    private final LocaleProvider localeProvider;

    @Override
    public BotApiMethod<? extends Serializable> create(Long userId, InitField<?> initField) {
        String text = messageProvider.getLocaledMessageForUser(
                userId,
                InfoMessage.FILL_INIT_FIELD,
                initField.name(),
                initField.type(),
                initField.description()
        );
        String format = formatTemplate.format(userId, (NumberFormat) initField.conf());
        if (StringUtils.isNotBlank(format)) {
            text += "; " + format;
        }

        SendMessage.SendMessageBuilder builder = SendMessage.builder()
                .text(text)
                .chatId(userId);
        if (!initField.required()) {
            ReplyKeyboardMarkup markup = keyboardManager.skipKeyboard(localeProvider.getLocaleByUserId(userId));
            builder.replyMarkup(markup);
        }
        return builder.build();
    }

}
