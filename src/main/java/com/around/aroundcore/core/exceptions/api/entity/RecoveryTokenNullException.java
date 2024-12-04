package com.around.aroundcore.core.exceptions.api.entity;

import com.around.aroundcore.core.enums.ApiResponse;

public class RecoveryTokenNullException extends EntityNullException{
    public RecoveryTokenNullException(){
        super("RecoveryToken is null", ApiResponse.INVALID_TOKEN);
    }

    public RecoveryTokenNullException(String message){
        super(message);
    }
}
