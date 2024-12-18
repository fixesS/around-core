package com.around.aroundcore.database.models.chunk;

import com.around.aroundcore.database.models.City;
import com.around.aroundcore.database.models.round.Round;
import com.around.aroundcore.database.models.user.GameUser;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "chunks", schema = "public")
@IdClass(GameChunkEmbedded.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GameChunk implements Serializable {

    @Id
    @Setter
    @Column
    private String id;

    @Id
    @Setter
    @ManyToOne()
    @JoinColumn(name = "round_id", referencedColumnName = "id")
    private Round round;

    @Setter
    @ManyToOne()
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    @ManyToOne()
    @JoinColumn(name = "owner", referencedColumnName = "id")
    private GameUser owner;

    @Override
    public String toString(){
        return String.format("chunk_id: %s ",this.id);
    }
}
