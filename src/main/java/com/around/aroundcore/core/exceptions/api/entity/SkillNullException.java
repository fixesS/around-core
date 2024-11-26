package com.around.aroundcore.core.exceptions.api.entity;

import com.around.aroundcore.core.enums.ApiResponse;

public class SkillNullException extends EntityNullException {
    public SkillNullException() {
        super("Skill is null", ApiResponse.SESSION_DOES_NOT_EXIST);
    }

    public SkillNullException(String message) {
        super(message);
    }
}
