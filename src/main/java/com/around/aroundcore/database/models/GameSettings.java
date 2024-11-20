package com.around.aroundcore.database.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Objects;

@Slf4j
@Entity
@Table(name = "game", schema = "settings")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameSettings implements Serializable {
    @Id
    private Integer id;
    @Column(name = "chunk_reward")
    private Integer chunkReward;
    @Column(name = "team_change_cost")
    private Integer teamChangeCost;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameSettings that = (GameSettings) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
