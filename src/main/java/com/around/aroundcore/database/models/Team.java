package com.around.aroundcore.database.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "team")
@Data
public class Team implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "color")
    private String color;

    @OneToMany(mappedBy = "team")
    private List<GameUser> members;

    @Override
    public String toString(){
        return String.format("team_id: %d, color: %s",this.id,this.color);
    }
}
