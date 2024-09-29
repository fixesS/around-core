package com.around.aroundcore.web.exceptions.entity;

import com.around.aroundcore.web.enums.ApiResponse;

public class GameUserSkillNullException extends EntityNullException {
    public GameUserSkillNullException() {
        super("GameUserSkills is null", ApiResponse.SKILL_DOES_NOT_EXIST);
    }

    public GameUserSkillNullException(String message) {
        super(message);
    }
}
