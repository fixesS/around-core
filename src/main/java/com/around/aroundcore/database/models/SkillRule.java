package com.around.aroundcore.database.models;

import com.around.aroundcore.database.converters.ListOfIntegerConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Table(name = "rules")
public class SkillRule implements Serializable {

    @Id
    private Integer id;

    @Column(name = "rule_value")
    @Convert(converter = ListOfIntegerConverter.class)
    private List<Integer> value;
}
