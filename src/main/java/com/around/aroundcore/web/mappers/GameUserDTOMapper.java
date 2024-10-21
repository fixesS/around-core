package com.around.aroundcore.web.mappers;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.UserRoundTeamCity;
import com.around.aroundcore.web.dtos.GameUserDTO;
import com.around.aroundcore.web.exceptions.entity.GameUserTeamCityNullForRound;
import com.around.aroundcore.web.exceptions.entity.NoActiveRoundException;
import com.around.aroundcore.web.exceptions.entity.RoundNullException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
public class GameUserDTOMapper implements Function<GameUser, GameUserDTO> {
    @Override
    public GameUserDTO apply(GameUser user) {
        return GameUserDTO.builder()
                .id(Optional.ofNullable(user.getId()).orElse(-1000))
                .avatar(AroundConfig.URL_AVATAR +user.getAvatar())
                .verified(user.getVerified())
                .email(Optional.ofNullable(user.getEmail()).orElse(""))
                .username(Optional.ofNullable(user.getUsername()).orElse(""))
                .city_id(getCityIdByUserForCurrentRound(user))
                .level(Optional.ofNullable(user.getLevel()).orElse(-1000))
                .coins(Optional.ofNullable(user.getCoins()).orElse(-1000))
                .team_id(getTeamIdByUserForCurrentRound(user))
                .captured_chunks(user.getCapturedChunks())
                .build();
    }
    private Integer getTeamIdByUserForCurrentRound(GameUser user){
        try{
            var team = UserRoundTeamCity.findTeamForCurrentRoundAndUser(user);
            return team.getId();
        }catch (NoActiveRoundException | RoundNullException | GameUserTeamCityNullForRound e){
            return -1000;
        }
    }
    private Integer getCityIdByUserForCurrentRound(GameUser user){
        try{
            var city = UserRoundTeamCity.findCityForCurrentRoundAndUser(user);
            return city.getId();
        }catch (NoActiveRoundException | RoundNullException | GameUserTeamCityNullForRound e){
            return -1000;
        }
    }
}
