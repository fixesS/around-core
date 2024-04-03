package com.around.aroundcore.web.exceptions.auth;

public class AuthHeaderNotStartsWithPrefixException extends AuthHeaderException{
    public AuthHeaderNotStartsWithPrefixException() {
        super();
    }

    public AuthHeaderNotStartsWithPrefixException(String message) {
        super(message);
        this.setMessage(message);
    }
}
