package com.around.aroundcore.web.exceptions.entity;

public class VerificationTokenNullException extends EntityNullException{
    public VerificationTokenNullException(){
        super("VerificationToken is null");
    }

    public VerificationTokenNullException(String message){
        super(message);
    }
}
