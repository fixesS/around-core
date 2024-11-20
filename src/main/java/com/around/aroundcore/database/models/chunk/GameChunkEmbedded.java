package com.around.aroundcore.database.models.chunk;

import com.around.aroundcore.database.models.round.Round;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameChunkEmbedded implements Serializable {
    private Round round;
    private String id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameChunkEmbedded that = (GameChunkEmbedded) o;
        return Objects.equals(round, that.round) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(round, id);
    }
}
