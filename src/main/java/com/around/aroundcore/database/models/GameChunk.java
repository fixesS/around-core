package com.around.aroundcore.database.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.IOException;
import java.io.Serializable;

@Entity
@Table(name = "chunks")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GameChunk implements Serializable {

    @Id
    @Setter
    @Column
    private String id;

    @Setter//todo разобраться
    @ManyToOne
    @JoinColumn(name = "round_id", referencedColumnName = "id")
    private Round round;

    @ManyToOne
    @JoinColumn(name = "owner", referencedColumnName = "id")
    private GameUser owner;

    @Override
    public String toString(){
        return String.format("chunk_id: %s ",this.id);
    }

    public Team getTeam(){
        return this.owner.getTeam(this.round);
    }
}
