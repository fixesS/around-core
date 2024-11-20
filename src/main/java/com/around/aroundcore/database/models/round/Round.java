package com.around.aroundcore.database.models.round;

import com.around.aroundcore.database.models.GameSettings;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "rounds", schema = "public")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Round implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_rounds_team_city",
            joinColumns = @JoinColumn(name = "round_id", referencedColumnName = "id"),
            inverseJoinColumns = {@JoinColumn(name = "round_id", referencedColumnName = "round_id",insertable=false, updatable=false),
                    @JoinColumn(name = "user_id", referencedColumnName = "user_id",insertable=false, updatable=false),
                    @JoinColumn(name = "team_id", referencedColumnName = "team_id",insertable=false, updatable=false),
                    @JoinColumn(name = "city_id", referencedColumnName = "city_id",insertable=false, updatable=false)}
    )
    private List<UserRoundTeamCity> userRoundTeamCity;

    @Column
    private String name;

    @Column
    private LocalDateTime starts;

    @Column
    private LocalDateTime ends;

    @Column
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "game_settings_id", referencedColumnName = "id")
    private GameSettings gameSettings;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Round round = (Round) o;
        return Objects.equals(id, round.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
