package org.quizstorage.exceptions;

import org.quizstorage.components.common.ErrorMessage;

public class InitFieldsContainerIsAbsentException extends QuizTelegramClientException {

    @Override
    public String[] getCodes() {
        return new String[] {ErrorMessage.INIT_FIELDS_CONTAINER_IS_ABSENT.getCode()};
    }

}
