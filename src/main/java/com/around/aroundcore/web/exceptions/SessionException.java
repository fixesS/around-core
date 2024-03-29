package com.around.aroundcore.web.exceptions;

import lombok.Getter;
import lombok.Setter;

public class SessionException extends RuntimeException{
    @Getter
    @Setter
    private String message;
    public SessionException() {
        super();
    }

    public SessionException(String message) {
        super(message);
        this.message = message;
    }
}
