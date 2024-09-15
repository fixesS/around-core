package com.around.aroundcore.database.models;

import com.around.aroundcore.web.exceptions.entity.GameUserTeamNullForRound;
import com.around.aroundcore.web.exceptions.entity.NoActiveRoundException;
import com.around.aroundcore.web.exceptions.entity.RoundNullException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

@Entity
@Table(name = "user_round_team")
@ToString
@IdClass(UserRoundTeamEmbedded.class)
public class UserRoundTeam implements Serializable {

//    @EmbeddedId
//    @Getter
//    @Setter
//    private UserRoundTeamEmbedded userRoundTeamEmbedded;

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

    public Round getRound(){
        return round;
    }
    public GameUser getUser(){
        return user;
    }
    public Team getTeam(){
        return team;
    }
    public static Round findCurrentRoundFromURTs(List<UserRoundTeam> urts) throws NoActiveRoundException {
        return urts.stream().filter(urt1->urt1.getRound().getActive()).findFirst().orElseThrow(NoActiveRoundException::new).getRound();
    }
    public static Team findTeamForCurrentRoundAndUser(GameUser user) throws RoundNullException, NoActiveRoundException, GameUserTeamNullForRound {
        var round = findCurrentRoundFromURTs(user.getUserRoundTeams());
        return user.getUserRoundTeams().stream().filter(urt -> urt.getUser().equals(user) && urt.getRound().equals(round)).findFirst().orElseThrow(GameUserTeamNullForRound::new).getTeam();
    }
    public static List<UserRoundTeam> getURTsDistinctByUser(List<UserRoundTeam> urts){
        return urts.stream().filter(distinctByKey(UserRoundTeam::getUser)).toList();
    }
    public static List<UserRoundTeam> getURTsDistinctByRound(List<UserRoundTeam> urts){
        return urts.stream().filter(distinctByKey(UserRoundTeam::getRound)).toList();
    }
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
