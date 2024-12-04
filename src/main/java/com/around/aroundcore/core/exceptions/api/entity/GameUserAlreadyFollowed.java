package com.around.aroundcore.core.exceptions.api.entity;

import com.around.aroundcore.core.enums.ApiResponse;

public class GameUserAlreadyFollowed extends EntityException{
    public GameUserAlreadyFollowed(){
        super("GameUser already followed", ApiResponse.USER_FOLLOW_ALREADY_EXIST);
    }
}
