package org.quizstorage.exceptions;

import org.quizstorage.components.common.ErrorMessage;

public class CurrentInitFieldValueHolderIsAbsent extends QuizTelegramClientException {

    @Override
    public String[] getCodes() {
        return new String[] {ErrorMessage.CURRENT_INIT_VALUE_HOLDER_IS_ABSENT.getCode()};
    }

}
