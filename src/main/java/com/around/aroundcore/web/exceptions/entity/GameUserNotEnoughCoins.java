package com.around.aroundcore.web.exceptions.entity;

import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;

public class GameUserNotEnoughCoins extends ApiException {
    public GameUserNotEnoughCoins(){
        super(ApiResponse.USER_NOT_ENOUGH_COINS);
    }
}
