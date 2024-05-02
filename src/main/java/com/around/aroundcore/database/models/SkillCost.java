package com.around.aroundcore.database.models;

import com.around.aroundcore.database.converters.ListOfIntegerConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "cost")
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
