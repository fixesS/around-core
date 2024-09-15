package com.around.aroundcore.web.exceptions.entity;

public class RoundNullException extends EntityNullException {
    public RoundNullException() {
        super("Round is null");
    }

    public RoundNullException(String message) {
        super(message);
    }
}
