package com.around.aroundcore.core.exceptions.api.entity;

import com.around.aroundcore.core.enums.ApiResponse;

public class NoActiveRoundException extends EntityException {
    public NoActiveRoundException() {
        super("There is no active round", ApiResponse.NO_ACTIVE_ROUND);
    }
}
