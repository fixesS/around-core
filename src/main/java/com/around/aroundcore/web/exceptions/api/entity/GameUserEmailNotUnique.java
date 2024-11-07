package com.around.aroundcore.web.exceptions.api.entity;

import com.around.aroundcore.web.enums.ApiResponse;

public class GameUserEmailNotUnique extends EntityFieldIsNotUniqueException{
    public GameUserEmailNotUnique(){
        super("GameUser email is now unique", ApiResponse.USER_ALREADY_EXIST);
    }

}
