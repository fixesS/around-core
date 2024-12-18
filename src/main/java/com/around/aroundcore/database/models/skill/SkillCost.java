package com.around.aroundcore.database.models.skill;

import com.around.aroundcore.database.converters.ListOfIntegerConverter;
import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Table(name = "costs", schema = "settings")
public class SkillCost implements Serializable {

    @Id
    private Integer id;

    @Column(name = "cost_value")
    @Convert(converter = ListOfIntegerConverter.class)
    private List<Integer> value;

    public Integer getCostForNewLevelByOldLevel(Integer oldLevel,Integer newLevel){
        Integer sum = 0;
        for (int i = oldLevel+1; i <= newLevel; i++) {
            sum += value.get(i);
        }
        return sum;
    }
}
