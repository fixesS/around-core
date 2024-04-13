package com.around.aroundcore.web.exceptions.entity;

public class RecoveryTokenNullException extends EntityNullException{
    public RecoveryTokenNullException(){
        super("RecoveryToken is null");
    }

    public RecoveryTokenNullException(String message){
        super(message);
    }
}
