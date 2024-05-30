package com.around.aroundcore.web.mappers;

import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.web.dtos.UserStatDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserStatDTOMapper implements Function<GameUser, UserStatDTO> {
    @Override
    public UserStatDTO apply(GameUser user) {
        return UserStatDTO.builder()
                .id(user.getId())
                .level(user.getLevel())
                .team_id(user.getTeam().getId())
                .username(user.getUsername())
                .avatar(user.getAvatar())
                .number(user.getCapturedChunks().size())
                .build();
    }
}
