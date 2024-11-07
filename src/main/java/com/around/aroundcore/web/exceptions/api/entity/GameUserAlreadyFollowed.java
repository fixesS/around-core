package com.around.aroundcore.web.exceptions.api.entity;

import com.around.aroundcore.web.enums.ApiResponse;

public class GameUserAlreadyFollowed extends EntityException{
    public GameUserAlreadyFollowed(){
        super("GameUser already followed", ApiResponse.USER_FOLLOW_ALREADY_EXIST);
    }
}
