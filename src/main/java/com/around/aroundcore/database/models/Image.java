package com.around.aroundcore.database.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@ToString
@Entity
@Table(name = "images", schema = "public")
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
    @Column
    @Builder.Default
    private Boolean is_default = false;

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}
