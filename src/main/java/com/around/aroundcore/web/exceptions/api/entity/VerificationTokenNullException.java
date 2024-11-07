package com.around.aroundcore.web.exceptions.api.entity;

import com.around.aroundcore.web.enums.ApiResponse;

public class VerificationTokenNullException extends EntityNullException{
    public VerificationTokenNullException(){
        super("VerificationToken is null", ApiResponse.INVALID_TOKEN);
    }

    public VerificationTokenNullException(String message){
        super(message);
    }
}
