package com.around.aroundcore.core.exceptions.auth;

import lombok.Getter;
import lombok.Setter;

public class AuthHeaderException extends RuntimeException{

    @Getter
    @Setter
    private String message;
    public AuthHeaderException() {
        super();
    }

    public AuthHeaderException(String message) {
        super(message);
        this.message = message;
    }
}
