package com.around.aroundcore.core.exceptions.api.entity;

import com.around.aroundcore.core.enums.ApiResponse;

public class GameSettingNullException extends EntityNullException {
    public GameSettingNullException() {
        super("Game setting is null", ApiResponse.GAME_SETTINGS_DOEST_NOT_EXIST);
    }
}
