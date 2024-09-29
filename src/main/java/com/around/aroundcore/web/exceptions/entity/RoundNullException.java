package com.around.aroundcore.web.exceptions.entity;

import com.around.aroundcore.web.enums.ApiResponse;

public class RoundNullException extends EntityNullException {
    public RoundNullException() {
        super("Round is null", ApiResponse.ROUND_DOES_NOT_EXIST);
    }

    public RoundNullException(String message) {
        super(message);
    }
}
