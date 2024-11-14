package com.around.aroundcore.database.models;

import com.around.aroundcore.web.exceptions.api.entity.GameUserTeamCityNullForRound;
import com.around.aroundcore.web.exceptions.api.entity.NoActiveRoundException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

@Entity
@Table(name = "users_rounds_team_city", schema = "public")
@ToString
@Getter
@IdClass(UserRoundTeamCityEmbedded.class)
public class UserRoundTeamCity implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "round_id", referencedColumnName = "id")
    private Round round;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private GameUser user;


    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;


    public static Round findCurrentRoundFromURTs(List<UserRoundTeamCity> urts) throws GameUserTeamCityNullForRound {
        return urts.stream().filter(urt1->urt1.getRound().getActive()).findFirst().orElseThrow(GameUserTeamCityNullForRound::new).getRound();
    }
    public static boolean checkIfExistCurrentURTCFromURTCs(List<UserRoundTeamCity> urts) {
        return urts.stream().anyMatch(urt1->urt1.getRound().getActive());
    }
    public static Team findTeamForCurrentRoundAndUser(GameUser user) throws NoActiveRoundException, GameUserTeamCityNullForRound {
        var round = findCurrentRoundFromURTs(user.getUserRoundTeamCities());
        return user.getUserRoundTeamCities().stream().filter(urt -> urt.getUser().equals(user) && urt.getRound().equals(round)).findFirst().orElseThrow(GameUserTeamCityNullForRound::new).getTeam();
    }
    public static City findCityForCurrentRoundAndUser(GameUser user) throws NoActiveRoundException, GameUserTeamCityNullForRound {
        var round = findCurrentRoundFromURTs(user.getUserRoundTeamCities());
        return user.getUserRoundTeamCities().stream().filter(urt -> urt.getUser().equals(user) && urt.getRound().equals(round)).findFirst().orElseThrow(GameUserTeamCityNullForRound::new).getCity();
    }
    public static List<UserRoundTeamCity> getURTsDistinctByUser(List<UserRoundTeamCity> urts){
        return urts.stream().filter(distinctByKey(UserRoundTeamCity::getUser)).toList();
    }
    public static List<UserRoundTeamCity> getURTsDistinctByRound(List<UserRoundTeamCity> urts){
        return urts.stream().filter(distinctByKey(UserRoundTeamCity::getRound)).toList();
    }
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
