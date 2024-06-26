package com.around.aroundcore.database.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "skill")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Skill implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "max_level")
    private Integer maxLevel;

    @Column(name = "description")
    private String description;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="rule_id")
    private SkillRule rule;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="cost_id")
    private SkillCost cost;
    @Column(name = "image_name")
    private String imageName;
    @Column(name = "icon")
    private String icon;
}
