package com.around.aroundcore.web.exceptions.entity;

public class GameUserUsernameNotUnique extends EntityFieldIsNotUniqueException{
    public GameUserUsernameNotUnique(){
        super("GameUser username is now unique");
    }

    public GameUserUsernameNotUnique(String message){
        super(message);
    }
}
