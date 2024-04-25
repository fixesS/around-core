package com.around.aroundcore.database.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event_categories")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Category {
    @Id
    @Column
    private Integer id;
    @Column
    private String name;
}
