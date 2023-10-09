package org.quizstorage.exceptions;

public class GameNotFoundException extends InternalServerException {

    public GameNotFoundException(String message) {
        super(message);
    }
}
