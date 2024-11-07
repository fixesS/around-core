package com.around.aroundcore.web.exceptions.api.entity;

import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;

public class GameUserSkillAlreadyMaxLevel extends ApiException {
    public GameUserSkillAlreadyMaxLevel() {
        super(ApiResponse.SKILL_LEVEL_ALREADY_MAX);
    }
}
