package com.around.aroundcore.database.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "skill")
@Data
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "max_level")
    private int maxLevel;

    @Column(name = "description")
    private String description;

    @Column(name = "upgrade_cost")
    private int upgradeCost;

    @Column(name = "image_name")
    private String imageName;
}
