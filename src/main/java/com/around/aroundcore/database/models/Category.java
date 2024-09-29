package com.around.aroundcore.database.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "event_categories")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Category {
    @Id
    @Column
    private Integer id;
    @Column
    private String name;
}
