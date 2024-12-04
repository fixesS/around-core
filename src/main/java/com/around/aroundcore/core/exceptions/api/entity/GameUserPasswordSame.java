package com.around.aroundcore.core.exceptions.api.entity;

import com.around.aroundcore.core.enums.ApiResponse;

public class GameUserPasswordSame extends EntityException{

    public GameUserPasswordSame() {
        super("GameUser password is the same as previous", ApiResponse.USER_NEW_PASSWORD_THE_SAME);
    }

    public GameUserPasswordSame(String message) {
        super(message);
    }
}
