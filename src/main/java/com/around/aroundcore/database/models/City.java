package com.around.aroundcore.database.models;

import com.around.aroundcore.database.converters.ListOfChunkDTOConverter;
import com.around.aroundcore.database.dtos.ChunkDTOForCity;
import com.around.aroundcore.database.models.round.UserRoundTeamCity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "cities", schema = "public")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class City implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_rounds_team_city",
            joinColumns = @JoinColumn(name = "city_id", referencedColumnName = "id"),
            inverseJoinColumns = {@JoinColumn(name = "city_id", referencedColumnName = "city_id",insertable=false, updatable=false),
                    @JoinColumn(name = "user_id", referencedColumnName = "user_id",insertable=false, updatable=false),
                    @JoinColumn(name = "team_id", referencedColumnName = "team_id",insertable=false, updatable=false),
                    @JoinColumn(name = "round_id", referencedColumnName = "round_id",insertable=false, updatable=false)}
    )
    private List<UserRoundTeamCity> userRoundTeamCity;

    @Column
    private String name;

    @Column
    private String locale;

    @Column(name = "chunks_resolution")
    private Integer chunksResolution;

    @Column(updatable = false)
    @Convert(converter = ListOfChunkDTOConverter.class)
    private List<ChunkDTOForCity> chunks;

    public boolean containsChunkDTO(String chunk) {
        return chunks.stream().map(ChunkDTOForCity::getId).toList().contains(chunk);
    }
}