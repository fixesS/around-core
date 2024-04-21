package com.around.aroundcore.database.models;

import jakarta.persistence.*;
import lombok.*;


import java.io.Serializable;

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


}
