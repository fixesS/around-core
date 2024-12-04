package com.around.aroundcore.database.models.user;

import com.around.aroundcore.database.models.skill.Skill;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Setter
@Getter
public class GameUserSkillEmbedded implements Serializable {

    @ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
    @JoinColumn(name = "user_id")
    private GameUser gameUser;

    @ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameUserSkillEmbedded that = (GameUserSkillEmbedded) o;
        return Objects.equals(gameUser, that.gameUser) && Objects.equals(skill, that.skill);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameUser, skill);
    }
}
