package com.around.aroundcore.database.models;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;


@Entity
@Table(name = "user_skills")
@Data
public class GameUserSkill implements Serializable {

    @EmbeddedId
    private GameUserSkillEmbedded gameUserSkillEmbedded;

    @Column(name = "current_level")
    private Integer currentLevel;

    public Integer getSkillId(){
        return gameUserSkillEmbedded.getSkill().getId();
    }
}
