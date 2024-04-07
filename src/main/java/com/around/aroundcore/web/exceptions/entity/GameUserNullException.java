package com.around.aroundcore.web.exceptions.entity;

public class GameUserNullException extends EntityNullException{
    public GameUserNullException(){
        super("GameUser is null");
    }

    public GameUserNullException(String message){
        super(message);
    }
}
