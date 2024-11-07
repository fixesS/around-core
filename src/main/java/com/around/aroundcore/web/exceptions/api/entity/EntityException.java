package com.around.aroundcore.web.exceptions.api.entity;

import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EntityException extends ApiException {
    private final String message;
    public EntityException() {
        super();
        this.message = "";
    }

    public EntityException(String message) {
        super();
        this.message = message;
    }
    public EntityException(String message, ApiResponse response) {
        super(response);
        this.message = message;
    }
}
