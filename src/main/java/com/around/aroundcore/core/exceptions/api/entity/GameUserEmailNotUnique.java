package com.around.aroundcore.core.exceptions.api.entity;

import com.around.aroundcore.core.enums.ApiResponse;

public class GameUserEmailNotUnique extends EntityFieldIsNotUniqueException{
    public GameUserEmailNotUnique(){
        super("GameUser email is now unique", ApiResponse.USER_ALREADY_EXIST);
    }

}
