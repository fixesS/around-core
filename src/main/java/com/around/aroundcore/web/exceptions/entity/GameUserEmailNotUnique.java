package com.around.aroundcore.web.exceptions.entity;

public class GameUserEmailNotUnique extends EntityFieldIsNotUniqueException{
    public GameUserEmailNotUnique(){
        super("GameUser email is now unique");
    }

    public GameUserEmailNotUnique(String message){
        super(message);
    }
}
