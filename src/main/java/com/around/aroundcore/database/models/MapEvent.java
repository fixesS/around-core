package com.around.aroundcore.database.models;

import com.around.aroundcore.annotations.NotRead;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "map_events")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MapEvent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @NotRead
    private Integer id;
    @Column
    private String name;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "provider_id", referencedColumnName = "id")
    private EventProvider provider;
    @Column(name = "starts_at")
    private LocalDateTime starts;
    @Column(name = "ends_at")
    private LocalDateTime ends;
    @Column
    private String url;
    @Column
    private boolean verified;
    @Column
    private boolean active;
    @Column(name = "ad")
    private boolean isAd;

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "map_events_chunks",
            joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "chunk_id", referencedColumnName = "id")
    )
    private List<GameChunk> chunks;
    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "map_events_categories",
            joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id")
    )
    private List<Category> categories;

}
