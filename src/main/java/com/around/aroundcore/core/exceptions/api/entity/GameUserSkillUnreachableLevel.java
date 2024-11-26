package com.around.aroundcore.core.exceptions.api.entity;

import com.around.aroundcore.core.enums.ApiResponse;
import com.around.aroundcore.core.exceptions.api.ApiException;

public class GameUserSkillUnreachableLevel extends ApiException {
    public GameUserSkillUnreachableLevel() {
        super(ApiResponse.SKILL_LEVEL_UNREACHABLE);
    }
}
