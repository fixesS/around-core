package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.models.Session;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.web.dto.ChunkDTO;
import com.around.aroundcore.web.dto.GameUserDTO;
import com.around.aroundcore.web.dto.ResetPasswordDTO;
import com.around.aroundcore.web.dto.TokenData;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.exceptions.entity.GameUserNullException;
import com.around.aroundcore.web.exceptions.entity.SessionNullException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AroundConfig.API_V1_USER)
@Tag(name="Game User Controller", description="Controller to get info about user")
@SecurityRequirement(name = "JWT")
public class GameUserController {
    private SessionService sessionService;
    private ObjectMapper objectMapper;

    @GetMapping("/me")
    @Operation(
            summary = "Gives all info about user",
            description = "Allows to get all info about user."
    )
    public ResponseEntity<GameUserDTO> getMe(){
        ApiResponse response;
        GameUserDTO gameUserDTO = null;

        try {
            var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var session = sessionService.findByUuid(sessionUuid);
            var user = session.getUser();
            gameUserDTO = GameUserDTO.builder()
                    .email(Optional.ofNullable(user.getEmail()).orElse(""))
                    .username(Optional.ofNullable(user.getUsername()).orElse(""))
                    .city(Optional.ofNullable(user.getCity()).orElse(""))
                    .level(Optional.ofNullable(user.getLevel()).orElse(-1000))
                    .coins(Optional.ofNullable(user.getCoins()).orElse(-1000))
                    .team_id(Optional.ofNullable(user.getTeam()).map(Team::getId).orElse(-1000))
                    .build();
            response = ApiResponse.OK;
        } catch (SessionNullException e) {
            response = ApiResponse.SESSION_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (GameUserNullException e) {
            response = ApiResponse.USER_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }

        switch (response) {
            case OK -> {
                return new ResponseEntity<>(gameUserDTO,response.getStatus());

            }
            default -> throw new ApiException(response);
        }
    }
  
    @GetMapping("/friends")
    @Operation(
            summary = "Gives friends",
            description = "Allows get info about all friends of user."
    )
    public ResponseEntity<List<GameUserDTO>> getFriends() {
        ApiResponse response;
        List<GameUserDTO> friends = null;

        try {
            var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var session = sessionService.findByUuid(sessionUuid);
            var user = session.getUser();
            friends = user.getFriends().stream().map(
                            gameUser ->
                                    GameUserDTO.builder()
                                            .username(gameUser.getUsername())
                                            .level(gameUser.getLevel())
                                            .team_id(gameUser.getTeam().getId())
                                            .coins(gameUser.getCoins())
                                            .email(gameUser.getEmail())
                                            .city(gameUser.getCity())
                                            .build()
                    )
                    .toList();
            response = ApiResponse.OK;
        } catch (SessionNullException e) {
            response = ApiResponse.SESSION_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (GameUserNullException e) {
            response = ApiResponse.USER_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }

        switch (response) {
            case OK -> {
                return new ResponseEntity<>(friends,response.getStatus());

            }
            default -> throw new ApiException(response);
        }
    }
}
