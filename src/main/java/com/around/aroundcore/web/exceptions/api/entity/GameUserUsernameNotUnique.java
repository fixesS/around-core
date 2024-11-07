package com.around.aroundcore.web.exceptions.api.entity;

import com.around.aroundcore.web.enums.ApiResponse;

public class GameUserUsernameNotUnique extends EntityFieldIsNotUniqueException{
    public GameUserUsernameNotUnique(){
        super("GameUser username is now unique", ApiResponse.USER_NOT_UNIQUE_USERNAME);
    }
}
