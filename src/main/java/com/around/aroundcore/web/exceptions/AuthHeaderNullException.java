package com.around.aroundcore.web.exceptions;

public class AuthHeaderNullException extends AuthHeaderException{
    public AuthHeaderNullException() {
        super();
    }

    public AuthHeaderNullException(String message) {
        super(message);
        this.setMessage(message);
    }
}
