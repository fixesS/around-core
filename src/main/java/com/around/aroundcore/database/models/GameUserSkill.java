package com.around.aroundcore.database.models;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "user_skills")
@Data
public class GameUserSkill {

    @EmbeddedId
    private GameUserSkillEmbedded gameUserSkillEmbedded;

    @Column(name = "current_level")
    private Integer currentLevel;

    public Integer getSkillId(){
        return gameUserSkillEmbedded.getSkill().getId();
    }
}
