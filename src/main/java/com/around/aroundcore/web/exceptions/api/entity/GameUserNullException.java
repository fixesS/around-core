package com.around.aroundcore.web.exceptions.api.entity;

import com.around.aroundcore.web.enums.ApiResponse;

public class GameUserNullException extends EntityNullException{
    public GameUserNullException(){
        super("GameUser is null", ApiResponse.USER_DOES_NOT_EXIST);
    }

    public GameUserNullException(String message){
        super(message);
    }
}
