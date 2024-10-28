package com.around.aroundcore.database.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@ToString
@Entity
@Table(name = "images")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Image implements Serializable {

    @Id
    @Builder.Default
    @Column(columnDefinition = "BINARY(16)", name = "uuid")
    private UUID uuid = UUID.randomUUID();
    @Column
    @Setter
    private String file;
    @Column
    @Setter
    private String url;
}
