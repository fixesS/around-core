package com.around.aroundcore.database.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "skills")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return Objects.equals(id, skill.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
