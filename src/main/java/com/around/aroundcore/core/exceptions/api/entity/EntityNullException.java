package com.around.aroundcore.core.exceptions.api.entity;

import com.around.aroundcore.core.enums.ApiResponse;

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
