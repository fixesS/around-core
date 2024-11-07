package com.around.aroundcore.web.exceptions.api.entity;

import com.around.aroundcore.web.enums.ApiResponse;

public class SkillNullException extends EntityNullException {
    public SkillNullException() {
        super("Skill is null", ApiResponse.SESSION_DOES_NOT_EXIST);
    }

    public SkillNullException(String message) {
        super(message);
    }
}
