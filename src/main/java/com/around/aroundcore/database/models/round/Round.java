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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="previous_round_id")
    private Round previousRound;

    @OneToOne(fetch = FetchType.LAZY)
    @Setter
    @JoinColumn(name="next_round_id")
    private Round nextRound;

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
    @Builder.Default
    @Setter
    private Boolean active = false;

    @ManyToOne
    @JoinColumn(name = "game_settings_id", referencedColumnName = "id")
    private GameSettings gameSettings;


    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
