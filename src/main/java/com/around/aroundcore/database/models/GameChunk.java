package com.around.aroundcore.database.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "game_chunk")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GameChunk implements Serializable {

    @Id
    @Column
    private String id;

    @ManyToOne
    @JoinColumn(name = "owner", referencedColumnName = "id")
    private GameUser owner;

    @Override
    public String toString(){
        return String.format("chunk_id: %s ",this.id);
    }
}
