package com.around.aroundcore.web.exceptions.entity;

public class SessionNullException extends EntityNullException {
    public SessionNullException() {
        super("Session is null");
    }

    public SessionNullException(String message) {
        super(message);
    }
}
