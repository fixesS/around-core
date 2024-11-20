package com.around.aroundcore.database.models.skill;

import com.around.aroundcore.database.models.Image;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "skills", schema = "public")
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
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(referencedColumnName = "uuid")
    private Image image;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(referencedColumnName = "uuid")
    private Image icon;

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
