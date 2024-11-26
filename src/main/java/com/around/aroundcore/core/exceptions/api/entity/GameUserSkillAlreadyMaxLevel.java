package com.around.aroundcore.core.exceptions.api.entity;

import com.around.aroundcore.core.enums.ApiResponse;
import com.around.aroundcore.core.exceptions.api.ApiException;

public class GameUserSkillAlreadyMaxLevel extends ApiException {
    public GameUserSkillAlreadyMaxLevel() {
        super(ApiResponse.SKILL_LEVEL_ALREADY_MAX);
    }
}
