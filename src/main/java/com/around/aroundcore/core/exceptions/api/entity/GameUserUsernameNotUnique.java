package com.around.aroundcore.core.exceptions.api.entity;

import com.around.aroundcore.core.enums.ApiResponse;

public class GameUserUsernameNotUnique extends EntityFieldIsNotUniqueException{
    public GameUserUsernameNotUnique(){
        super("GameUser username is now unique", ApiResponse.USER_NOT_UNIQUE_USERNAME);
    }
}
