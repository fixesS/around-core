package com.around.aroundcore.database.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;

@Getter
@Entity
@Table(name = "map_events_category")
public class MapEventCategories {

    @Id
    @Column
    private Integer event_id;

    @Id
    @Column
    private Integer category_id;
}
