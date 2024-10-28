package com.around.aroundcore.database.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.Objects;

@Getter
@Entity
@Table(name = "map_events_categories")
public class MapEventCategories {

    @Id
    @Column
    private Integer event_id;

    @Id
    @Column
    private Integer category_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapEventCategories that = (MapEventCategories) o;
        return Objects.equals(event_id, that.event_id) && Objects.equals(category_id, that.category_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event_id, category_id);
    }
}
