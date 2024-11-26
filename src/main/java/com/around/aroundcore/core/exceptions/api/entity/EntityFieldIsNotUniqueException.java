package com.around.aroundcore.core.exceptions.api.entity;

import com.around.aroundcore.core.enums.ApiResponse;

public class EntityFieldIsNotUniqueException extends EntityException{
    public EntityFieldIsNotUniqueException() {
        super();
    }

    public EntityFieldIsNotUniqueException(String message, ApiResponse response) {
        super(message, response);
    }

}
