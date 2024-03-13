package com.around.aroundcore.database.models;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class GameUserSkillEmbedded implements Serializable {

    private GameUser gameUser;
    private Skill skill;
}
