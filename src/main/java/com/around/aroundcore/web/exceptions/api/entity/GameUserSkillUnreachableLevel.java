package com.around.aroundcore.web.exceptions.api.entity;

import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;

public class GameUserSkillUnreachableLevel extends ApiException {
    public GameUserSkillUnreachableLevel() {
        super(ApiResponse.SKILL_LEVEL_UNREACHABLE);
    }
}
