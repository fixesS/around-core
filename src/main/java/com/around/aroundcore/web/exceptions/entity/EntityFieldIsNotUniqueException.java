package com.around.aroundcore.web.exceptions.entity;

import com.around.aroundcore.web.enums.ApiResponse;

public class EntityFieldIsNotUniqueException extends EntityException{
    public EntityFieldIsNotUniqueException() {
        super();
    }

    public EntityFieldIsNotUniqueException(String message, ApiResponse response) {
        super(message, response);
    }

}
