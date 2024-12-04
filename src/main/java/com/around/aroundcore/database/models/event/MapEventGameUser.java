package com.around.aroundcore.database.models.event;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.Objects;

@Getter
@Entity
@Table(name = "events_users", schema = "map_events")
public class MapEventGameUser {
    @Id
    @Column(name = "user_id")
    private int user_id;
    @Id
    @Column(name = "event_id")
    private int event_id;

    @Override
    public int hashCode() {
        return Objects.hash(user_id, event_id);
    }
}

