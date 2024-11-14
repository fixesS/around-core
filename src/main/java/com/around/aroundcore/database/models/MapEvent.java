package com.around.aroundcore.database.models;

import com.around.aroundcore.annotations.NotRead;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events", schema = "map_events")
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
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(referencedColumnName = "uuid")
    private Image image;
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
    @Column
    private Integer reward;

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "events_chunks", schema = "map_events",
            joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "chunk_id", referencedColumnName = "id")
    )
    private List<GameChunk> chunks;
    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "events_categories", schema = "map_events",
            joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id")
    )
    private List<Category> categories;

}
