package com.around.aroundcore.web.exceptions.entity;

public class GameUserSkillsNullException extends EntityNullException {
    public GameUserSkillsNullException() {
        super("GameUserSkills is null");
    }

    public GameUserSkillsNullException(String message) {
        super(message);
    }
}
