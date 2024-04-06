package com.around.aroundcore.web.exceptions.entity;

public class GameUserUsernameNotUnique extends EntityFieldIsNotUniqueException{
    public GameUserUsernameNotUnique(){
    }

    public GameUserUsernameNotUnique(String message){
        super(message);
    }
}
