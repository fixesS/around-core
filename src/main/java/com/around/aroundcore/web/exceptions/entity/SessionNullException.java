package com.around.aroundcore.web.exceptions.entity;

import com.around.aroundcore.web.enums.ApiResponse;

public class SessionNullException extends EntityNullException {
    public SessionNullException() {
        super("Session is null", ApiResponse.SESSION_DOES_NOT_EXIST);
    }

    public SessionNullException(String message) {
        super(message);
    }
}
