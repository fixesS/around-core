package com.around.aroundcore.core.exceptions.api.entity;

import com.around.aroundcore.core.enums.ApiResponse;

public class GameUserSkillNullException extends EntityNullException {
    public GameUserSkillNullException() {
        super("GameUserSkills is null", ApiResponse.SKILL_DOES_NOT_EXIST);
    }

    public GameUserSkillNullException(String message) {
        super(message);
    }
}
