package com.around.aroundcore.web.exceptions.api.entity;

import com.around.aroundcore.web.enums.ApiResponse;

public class NoActiveRoundException extends EntityException {
    public NoActiveRoundException() {
        super("There is no active round", ApiResponse.NO_ACTIVE_ROUND);
    }

    public NoActiveRoundException(String message) {
        super(message);
    }
}
