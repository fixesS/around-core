package com.around.aroundcore.core.exceptions.api.entity;

import com.around.aroundcore.core.enums.ApiResponse;

public class VerificationTokenNullException extends EntityNullException{
    public VerificationTokenNullException(){
        super("VerificationToken is null", ApiResponse.INVALID_TOKEN);
    }

    public VerificationTokenNullException(String message){
        super(message);
    }
}
