package com.around.aroundcore.web.exceptions.entity;

import com.around.aroundcore.web.enums.ApiResponse;

public class GameUserPasswordSame extends EntityException{

    public GameUserPasswordSame() {
        super("GameUser password is the same as previous", ApiResponse.USER_NEW_PASSWORD_THE_SAME);
    }

    public GameUserPasswordSame(String message) {
        super(message);
    }
}
