package com.around.aroundcore.web.exceptions.entity;

import lombok.Getter;
import lombok.Setter;

public class EntityException extends RuntimeException{
    @Getter
    @Setter
    private String message;
    public EntityException() {
        super();
    }

    public EntityException(String message) {
        super();
        this.message = message;
    }
}
