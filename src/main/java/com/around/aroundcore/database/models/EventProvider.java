package com.around.aroundcore.database.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "event_providers")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EventProvider implements Serializable {
    @Id
    private Integer id;
    @Column
    private String name;
    @Column
    private String url;
}
