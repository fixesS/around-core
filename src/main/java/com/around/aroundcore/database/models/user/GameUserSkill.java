package com.around.aroundcore.database.models.user;

import com.around.aroundcore.core.exceptions.api.entity.GameUserSkillAlreadyMaxLevel;
import com.around.aroundcore.core.exceptions.api.entity.GameUserSkillUnreachableLevel;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "users_skills", schema = "public")
@Getter
@Setter
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
            throw new GameUserSkillUnreachableLevel();
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
