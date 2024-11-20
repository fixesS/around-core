package com.around.aroundcore.database.models.event;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "categories", schema = "map_events")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Category implements Serializable {
    @Id
    @Column
    private Integer id;
    @Column
    private String name;
}
