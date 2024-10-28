package com.around.aroundcore.web.exceptions.entity;

import com.around.aroundcore.web.enums.ApiResponse;

public class EntityNullException extends EntityException{

    public EntityNullException() {
        super();
    }
    public EntityNullException(String message, ApiResponse response) {
        super(message, response);
    }

    public EntityNullException(String message) {
        super(message);
    }
}
