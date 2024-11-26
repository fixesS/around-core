package com.around.aroundcore.core.exceptions.api.entity;

import com.around.aroundcore.core.enums.ApiResponse;

public class SessionNullException extends EntityNullException {
    public SessionNullException() {
        super("Session is null", ApiResponse.SESSION_DOES_NOT_EXIST);
    }

    public SessionNullException(String message) {
        super(message);
    }
}
