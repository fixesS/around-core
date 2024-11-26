package com.around.aroundcore.core.exceptions.api.entity;

import com.around.aroundcore.core.enums.ApiResponse;

public class GameUserNullException extends EntityNullException{
    public GameUserNullException(){
        super("GameUser is null", ApiResponse.USER_DOES_NOT_EXIST);
    }

    public GameUserNullException(String message){
        super(message);
    }
}
