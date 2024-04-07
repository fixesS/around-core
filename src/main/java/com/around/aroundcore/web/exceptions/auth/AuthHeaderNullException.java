package com.around.aroundcore.web.exceptions.auth;

public class AuthHeaderNullException extends AuthHeaderException{
    public AuthHeaderNullException() {
        super("Auth header is null");
    }

    public AuthHeaderNullException(String message) {
        super(message);
        this.setMessage(message);
    }
}
