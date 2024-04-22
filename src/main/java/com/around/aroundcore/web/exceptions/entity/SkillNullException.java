package com.around.aroundcore.web.exceptions.entity;

public class SkillNullException extends EntityNullException {
    public SkillNullException() {
        super("Skill is null");
    }

    public SkillNullException(String message) {
        super(message);
    }
}
