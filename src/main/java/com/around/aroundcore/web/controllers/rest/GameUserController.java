package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.models.Session;
import com.around.aroundcore.database.services.GameUserService;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.web.dto.ApiOk;
import com.around.aroundcore.web.dto.GameUserDTO;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.exceptions.entity.GameUserNullException;
import com.around.aroundcore.web.exceptions.entity.SessionNullException;
import com.around.aroundcore.web.gson.GsonParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;
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

    @GetMapping("/me")
    @Operation(
            summary = "Gives all info about certain user",
            description = "Allows to get all info about certain user."
    )
    public ResponseEntity<GameUserDTO> handleGetMe(){
        ApiResponse response;
        String body = "";
        GameUser user = null;
        Session session;

        UUID sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            session = sessionService.findByUuid(sessionUuid);
            user = session.getUser();
            response = ApiResponse.OK;
        } catch (SessionNullException e) {
            response = ApiResponse.SESSION_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (GameUserNullException e) {
            response = ApiResponse.USER_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (Exception e) {
            response = ApiResponse.UNKNOWN_ERROR;
            log.error(e.getMessage());
        }
        switch (response) {
            case OK -> {
                GameUserDTO gameUserDTO = GameUserDTO.builder()
                        .email(Optional.ofNullable(user.getEmail()).orElse(""))
                        .username(Optional.ofNullable(user.getUsername()).orElse(""))
                        .city(Optional.ofNullable(user.getCity()).orElse(""))
                        .level(Optional.ofNullable(user.getLevel()).orElse(-1000))
                        .coins(Optional.ofNullable(user.getCoins()).orElse(-1000))
                        .team_id(Optional.ofNullable(user.getTeam()).map(Team::getId).orElse(-1000))
                        .build();

                return new ResponseEntity<>(gameUserDTO,response.getStatus());

            }
            default -> throw new ApiException(response);
        }

        return new ResponseEntity<>(body, response.getStatus());
    }
  
    @GetMapping("/friends")
    public ResponseEntity<?> indexFriends() {
        ResponseEntity<?> response;
        Session session;

        var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            session = sessionService.findByUuid(sessionUuid);
            var user = session.getUser();
            response = ResponseEntity.ok(user.getFriends()
                    .stream()
                    .map(f -> modelMapper.map(f, GameUserDTO.class))
                    .toList());
        } catch (SessionNullException e) {
            response = new ResponseEntity<>(ApiResponse.SESSION_DOES_NOT_EXIST.getMessage(),
                    ApiResponse.SESSION_DOES_NOT_EXIST.getStatus());
            log.error(e.getMessage());
        } catch (GameUserNullException e) {
            response = new ResponseEntity<>(ApiResponse.USER_DOES_NOT_EXIST.getMessage(),
                    ApiResponse.USER_DOES_NOT_EXIST.getStatus());
            log.error(e.getMessage());
        }

        return response;
    }
  
    @GetMapping("/chunks-state")
    public ResponseEntity<?> indexChunks() {
        ResponseEntity<?> response;
        Session session;

        var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            session = sessionService.findByUuid(sessionUuid);
            var user = session.getUser();
            response = ResponseEntity.ok(
                    user.getCapturedChunks()
                            .addAll(user.getTeam()
                                    .getMembers()
                                    .stream()
                                    .map(GameUser::getCapturedChunks)
                                    .flatMap(Collection::stream)
                                    .toList()
                            )
            );
        } catch (SessionNullException e) {
            response = new ResponseEntity<>(ApiResponse.SESSION_DOES_NOT_EXIST.getMessage(),
                    ApiResponse.SESSION_DOES_NOT_EXIST.getStatus());
            log.error(e.getMessage());
        } catch (GameUserNullException e) {
            response = new ResponseEntity<>(ApiResponse.USER_DOES_NOT_EXIST.getMessage(),
                    ApiResponse.USER_DOES_NOT_EXIST.getStatus());
            log.error(e.getMessage());
        } catch (NullPointerException e) {
            response = new ResponseEntity<>(ApiResponse.USER_HAS_NO_TEAM.getMessage(),
                    ApiResponse.USER_HAS_NO_TEAM.getStatus());
            log.error(e.getMessage());
        }

        return response;
    }
}
