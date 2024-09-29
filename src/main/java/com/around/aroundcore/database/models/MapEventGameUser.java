package com.around.aroundcore.database.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;

@Getter
@Entity
@Table(name = "map_events_game_user")
public class MapEventGameUser {
    @Id
    @Column(name = "user_id")
    private int user_id;
    @Id
    @Column(name = "event_id")
    private int event_id;
}

