package com.around.aroundcore.web.mappers;

import com.around.aroundcore.database.models.user.GameUser;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.models.round.UserRoundTeamCity;
import com.around.aroundcore.web.dtos.TeamDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
public class TeamDTOMapper implements Function<Team, TeamDTO> {
    @Override
    public TeamDTO apply(Team team) {
        return TeamDTO.builder()
                .id(Optional.ofNullable(team.getId()).orElse(-1000))
                .color(Optional.ofNullable(team.getColor()).orElse(""))
                .members(team.getUserRoundTeamCity().stream().filter(urt->urt.getRound().getActive()).map(UserRoundTeamCity::getUser).map(GameUser::getId).toList())
                .build();
    }
}
