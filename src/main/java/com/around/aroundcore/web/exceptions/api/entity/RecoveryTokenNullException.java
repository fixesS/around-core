package com.around.aroundcore.web.exceptions.api.entity;

import com.around.aroundcore.web.enums.ApiResponse;

public class RecoveryTokenNullException extends EntityNullException{
    public RecoveryTokenNullException(){
        super("RecoveryToken is null", ApiResponse.INVALID_TOKEN);
    }

    public RecoveryTokenNullException(String message){
        super(message);
    }
}
