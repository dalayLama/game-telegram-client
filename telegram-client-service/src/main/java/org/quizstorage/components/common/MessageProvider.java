package org.quizstorage.components.common;

import org.springframework.context.MessageSourceResolvable;

import java.util.Locale;

public interface MessageProvider {

    String getLocaledMessageForUser(Long userId, CoddedMessage message, Object...args);

    String getLocaledMessage(Locale locale, CoddedMessage coddedMessage, Object...args);

    String getLocaledMessageForUser(Long userId, MessageSourceResolvable messageSourceResolvable);

    String getLocaledMessage(Locale locale, MessageSourceResolvable messageSourceResolvable);

}
