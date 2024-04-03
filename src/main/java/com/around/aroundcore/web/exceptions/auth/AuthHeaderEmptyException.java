package com.around.aroundcore.web.exceptions.auth;

public class AuthHeaderEmptyException extends AuthHeaderException{
    public AuthHeaderEmptyException() {
        super();
    }

    public AuthHeaderEmptyException(String message) {
        super(message);
        this.setMessage(message);
    }
}
