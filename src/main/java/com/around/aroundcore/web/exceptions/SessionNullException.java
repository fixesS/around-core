package com.around.aroundcore.web.exceptions;

public class SessionNullException extends SessionException{
    public SessionNullException() {
        super();
    }

    public SessionNullException(String message) {
        super(message);
        this.setMessage(message);
    }
}
