package com.around.aroundcore.core.exceptions.api.entity;

import com.around.aroundcore.core.enums.ApiResponse;

public class RoundNullException extends EntityNullException {
    public RoundNullException() {
        super("Round is null", ApiResponse.ROUND_DOES_NOT_EXIST);
    }

    public RoundNullException(String message) {
        super(message);
    }
}
