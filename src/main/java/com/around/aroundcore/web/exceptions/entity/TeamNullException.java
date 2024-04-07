package com.around.aroundcore.web.exceptions.entity;

public class TeamNullException extends EntityNullException{
    public TeamNullException(){
        super("Team is null");
    }

    public TeamNullException(String message){
        super(message);
    }
}
