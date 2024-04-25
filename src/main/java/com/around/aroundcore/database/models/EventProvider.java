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
@Table(name = "event_providers")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventProvider {
    @Id
    private Integer id;
    @Column
    private String name;
    @Column
    private String url;
}
