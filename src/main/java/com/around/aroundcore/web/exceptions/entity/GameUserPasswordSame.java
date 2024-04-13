package com.around.aroundcore.web.exceptions.entity;

public class GameUserPasswordSame extends EntityException{

    public GameUserPasswordSame() {
        super("GameUser password is the same as previous");
    }

    public GameUserPasswordSame(String message) {
        super(message);
    }
}
