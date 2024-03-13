package com.around.aroundcore.database.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "team")
@Data
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "color")
    private String color;

    @OneToMany(mappedBy = "team")
    private List<GameUser> members;
}
