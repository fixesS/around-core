package com.around.aroundcore.core.exceptions.auth;

public class AuthHeaderEmptyException extends AuthHeaderException{
    public AuthHeaderEmptyException() {
        super("Auth header is empty.");
    }

    public AuthHeaderEmptyException(String message) {
        super(message);
        this.setMessage(message);
    }
}
