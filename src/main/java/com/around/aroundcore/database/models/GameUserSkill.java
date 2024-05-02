package com.around.aroundcore.database.models;

import com.around.aroundcore.web.exceptions.entity.GameUserSkillAlreadyMaxLevel;
import com.around.aroundcore.web.exceptions.entity.GameUserSkillUnreachebleLevel;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "user_skills")
@Data
public class GameUserSkill implements Serializable {

    @EmbeddedId
    private GameUserSkillEmbedded gameUserSkillEmbedded;

    @Column(name = "current_level")
    private Integer currentLevel;

    private void checkFutureLevel(Integer value){
        if(Objects.equals(currentLevel, getGameUserSkillEmbedded().getSkill().getMaxLevel())){
            throw new GameUserSkillAlreadyMaxLevel();
        }
        if(currentLevel+value > getGameUserSkillEmbedded().getSkill().getMaxLevel()){
            throw new GameUserSkillUnreachebleLevel();
        }

    }
    public Integer getFutureLevel(Integer value){
        checkFutureLevel(value);
        return currentLevel+value;
    }
    public void addLevel(Integer value){
        checkFutureLevel(value);
        currentLevel+=value;
    }
}
