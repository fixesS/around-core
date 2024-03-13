package com.around.aroundcore.database.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_skills")
@Data
public class GameUserSkills {

    @EmbeddedId
    private GameUserSkillEmbedded gameUserSkillEmbedded;

    @Column(name = "current_level")
    private int currentLevel;
}
