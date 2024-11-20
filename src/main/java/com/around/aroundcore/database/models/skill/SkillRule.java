package com.around.aroundcore.database.models.skill;

import com.around.aroundcore.database.converters.ListOfIntegerConverter;
import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Table(name = "rules", schema = "settings")
public class SkillRule implements Serializable {

    @Id
    private Integer id;

    @Column(name = "rule_value")
    @Convert(converter = ListOfIntegerConverter.class)
    private List<Integer> value;
}
