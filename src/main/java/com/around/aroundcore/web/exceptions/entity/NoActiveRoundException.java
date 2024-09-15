package com.around.aroundcore.web.exceptions.entity;

public class NoActiveRoundException extends RuntimeException {
    public NoActiveRoundException() {
        super("There is no active round");
    }

    public NoActiveRoundException(String message) {
        super(message);
    }
}
