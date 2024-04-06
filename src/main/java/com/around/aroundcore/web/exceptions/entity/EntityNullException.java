package com.around.aroundcore.web.exceptions.entity;

import lombok.Getter;
import lombok.Setter;

public class EntityNullException extends EntityException{

    public EntityNullException() {
        super();
    }

    public EntityNullException(String message) {
        super(message);
    }
}
