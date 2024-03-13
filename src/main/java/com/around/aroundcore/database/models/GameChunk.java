package com.around.aroundcore.database.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "game_chunk")
@Data
public class GameChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "owner", referencedColumnName = "id")
    private GameUser owner;
}
