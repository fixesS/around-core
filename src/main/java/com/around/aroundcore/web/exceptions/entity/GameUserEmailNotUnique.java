package com.around.aroundcore.web.exceptions.entity;

public class GameUserEmailNotUnique extends EntityFieldIsNotUniqueException{
    public GameUserEmailNotUnique(){
    }

    public GameUserEmailNotUnique(String message){
        super(message);
    }
}
