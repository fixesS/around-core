package com.around.aroundcore.web.exceptions.api.entity;

import com.around.aroundcore.web.enums.ApiResponse;

public class URTNullException extends EntityNullException {
    public URTNullException() {
        super("user-round-team relation does not exist", ApiResponse.USER_HAS_NO_TEAM_IN_ROUND);
    }

    public URTNullException(String message) {
        super(message);
    }
}
