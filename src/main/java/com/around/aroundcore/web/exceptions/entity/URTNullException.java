package com.around.aroundcore.web.exceptions.entity;

public class URTNullException extends EntityNullException {
    public URTNullException() {
        super("user-round-team relation does not exist");
    }

    public URTNullException(String message) {
        super(message);
    }
}
