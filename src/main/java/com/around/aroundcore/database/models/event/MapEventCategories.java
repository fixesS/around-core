package com.around.aroundcore.database.models.event;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.Objects;

@Getter
@Entity
@Table(name = "events_categories", schema = "map_events")
public class MapEventCategories {

    @Id
    @Column
    private Integer event_id;

    @Id
    @Column
    private Integer category_id;


    @Override
    public int hashCode() {
        return Objects.hash(event_id, category_id);
    }
}
