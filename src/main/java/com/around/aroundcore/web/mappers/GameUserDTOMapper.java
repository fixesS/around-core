package com.around.aroundcore.web.mappers;

import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.web.dtos.GameUserDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
public class GameUserDTOMapper implements Function<GameUser, GameUserDTO> {
    @Override
    public GameUserDTO apply(GameUser user) {
        return GameUserDTO.builder()
                .email(Optional.ofNullable(user.getEmail()).orElse(""))
                .username(Optional.ofNullable(user.getUsername()).orElse(""))
                .city(Optional.ofNullable(user.getCity()).orElse(""))
                .level(Optional.ofNullable(user.getLevel()).orElse(-1000))
                .coins(Optional.ofNullable(user.getCoins()).orElse(-1000))
                .team_id(Optional.ofNullable(user.getTeam()).map(Team::getId).orElse(-1000))
                .build();
    }
}
