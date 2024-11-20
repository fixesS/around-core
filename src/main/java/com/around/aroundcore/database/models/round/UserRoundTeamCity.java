package com.around.aroundcore.database.models.round;

import com.around.aroundcore.database.models.City;
import com.around.aroundcore.database.models.user.GameUser;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.web.exceptions.api.entity.GameUserTeamNullForRoundAndAneCity;
import com.around.aroundcore.web.exceptions.api.entity.NoActiveRoundException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;


@Entity
@Table(name = "users_rounds_team_city", schema = "public")
@FilterDef(name = "activeRoundFilter",parameters = @ParamDef(name = "active", type = Boolean.class), defaultCondition="round_id IN (SELECT r.id FROM rounds r WHERE r.active = :active)")
@IdClass(UserRoundTeamCityEmbedded.class)
@AllArgsConstructor
@NoArgsConstructor
public class UserRoundTeamCity implements Serializable {

    @Id
    @ManyToOne
    @Getter
    @JoinColumn(name = "round_id")
    private Round round;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @Getter
    @JoinColumn(name = "user_id")
    private GameUser user;

    @Id
    @ManyToOne
    @Getter
    @JoinColumn(name = "city_id")
    private City city;

    @ManyToOne
    @Getter
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "captured_chunks",nullable = false)
    @Getter
    private Long capturedChunks = 0L;

    public static Team findTeamByAnyCityForUser(GameUser user) throws NoActiveRoundException, GameUserTeamNullForRoundAndAneCity {
        return user.getUserRoundTeamCities().stream().findFirst().orElseThrow(GameUserTeamNullForRoundAndAneCity::new).getTeam();
    }
    public static Team findTeamByCityForUser(GameUser user, City city) throws NoActiveRoundException, GameUserTeamNullForRoundAndAneCity {
        return user.getUserRoundTeamCities().stream().filter(urtc ->  urtc.getCity().equals(city)).findFirst().orElseThrow(GameUserTeamNullForRoundAndAneCity::new).getTeam();
    }
    public static List<City> findCitiesForUser(GameUser user) throws NoActiveRoundException, GameUserTeamNullForRoundAndAneCity {
        return user.getUserRoundTeamCities().stream().filter(urt -> urt.getUser().equals(user)).map(UserRoundTeamCity::getCity).toList();
    }
    public static List<UserRoundTeamCity> getURTsDistinctByUser(List<UserRoundTeamCity> urts){
        return urts.stream().filter(distinctByKey(UserRoundTeamCity::getUser)).toList();
    }
    public static List<UserRoundTeamCity> getURTsDistinctByCity(List<UserRoundTeamCity> urts){
        return urts.stream().filter(distinctByKey(UserRoundTeamCity::getCity)).toList();
    }
    public static List<UserRoundTeamCity> getURTsDistinctByRound(List<UserRoundTeamCity> urts){
        return urts.stream().filter(distinctByKey(UserRoundTeamCity::getRound)).toList();
    }
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
