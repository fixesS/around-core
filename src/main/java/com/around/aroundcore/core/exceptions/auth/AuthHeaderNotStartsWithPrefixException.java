package com.around.aroundcore.core.exceptions.auth;

public class AuthHeaderNotStartsWithPrefixException extends AuthHeaderException{
    public AuthHeaderNotStartsWithPrefixException() {
        super("Auth header does not starts with prefix");
    }

    public AuthHeaderNotStartsWithPrefixException(String message) {
        super(message);
        this.setMessage(message);
    }
}
