package com.around.aroundcore.core.exceptions.api.entity;

import com.around.aroundcore.core.enums.ApiResponse;
import com.around.aroundcore.core.exceptions.api.ApiException;

public class GameUserNotEnoughCoins extends ApiException {
    public GameUserNotEnoughCoins(){
        super(ApiResponse.USER_NOT_ENOUGH_COINS);
    }
}
