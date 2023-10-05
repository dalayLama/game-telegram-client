package org.quizstorage.exceptions;

public class PersistingContextException extends InternalServerException {
    public PersistingContextException(Throwable e) {
        super(e);
    }

}
