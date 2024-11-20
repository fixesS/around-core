package com.around.aroundcore.web.mappers;

import com.around.aroundcore.database.models.user.GameUser;
import com.around.aroundcore.database.models.round.UserRoundTeamCity;
import com.around.aroundcore.web.dtos.user.GameUserDTO;
import com.around.aroundcore.web.dtos.user.GameUserOAuthProvider;
import com.around.aroundcore.web.dtos.user.UserCityDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class GameUserDTOMapper implements Function<GameUser, GameUserDTO> {
    @Override
    public GameUserDTO apply(GameUser user) {
        return GameUserDTO.builder()
                .id(Optional.ofNullable(user.getId()).orElse(-1000))
                .avatar(user.getAvatar().getUrl())
                .verified(user.getVerified())
                .email(Optional.ofNullable(user.getEmail()).orElse(""))
                .username(Optional.ofNullable(user.getUsername()).orElse(""))
                .cities(getCitiesForUser(user))
                .level(Optional.ofNullable(user.getLevel()).orElse(-1000))
                .coins(Optional.ofNullable(user.getCoins()).orElse(-1000))
                .providers(getProviders(user))
                .build();
    }
    private List<GameUserOAuthProvider> getProviders(GameUser user) {
        return user.getOAuths().stream().map(oAuthUser -> new GameUserOAuthProvider(oAuthUser.getProvider().name())).toList();

    }
    private List<UserCityDTO> getCitiesForUser(GameUser user){
        List<UserCityDTO> cities = new ArrayList<>();
        var urtcs = UserRoundTeamCity.getURTsDistinctByCity(user.getUserRoundTeamCities());
        urtcs.forEach(urtc -> cities.add(UserCityDTO.builder()
                .city_id(urtc.getCity().getId())
                .team_id(urtc.getTeam().getId())
                .captured_chunks(urtc.getCapturedChunks())
                .build()));
        return cities;
    }
}
