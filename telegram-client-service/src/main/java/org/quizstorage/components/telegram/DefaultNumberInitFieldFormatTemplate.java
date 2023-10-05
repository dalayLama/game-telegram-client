package org.quizstorage.components.telegram;

import lombok.RequiredArgsConstructor;
import org.quizstorage.components.common.InfoMessage;
import org.quizstorage.components.common.MessageProvider;
import org.quizstorage.generator.dto.NumberFormat;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultNumberInitFieldFormatTemplate implements NumberInitFieldFormatTemplate {

    private final MessageProvider messageProvider;

    @Override
    public String format(Long userId, NumberFormat format) {
        if (format == null) {
            return null;
        }
        return messageProvider.getLocaledMessageForUser(
                userId,
                InfoMessage.NUMBER_FORMAT,
                format.minValue(),
                format.maxValue()
        );
    }
}
