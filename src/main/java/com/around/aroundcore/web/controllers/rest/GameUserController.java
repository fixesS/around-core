package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.models.Session;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.web.dto.ApiError;
import com.around.aroundcore.web.dto.ApiOk;
import com.around.aroundcore.web.dto.GameUserDTO;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.ApiException;
import com.around.aroundcore.web.gson.GsonParser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AroundConfig.API_V1_USER)
public class GameUserController {

    private GsonParser gsonParser;
    private SessionService sessionService;

    @GetMapping("/me")
    public ResponseEntity<String> handleGetMe(HttpServletRequest request) throws UnknownHostException {
        ApiResponse response;
        String body = "";
        GameUser user = null;

        UUID sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Session session = sessionService.findByUuid(sessionUuid);
        try {
            if(session==null){
                response = ApiResponse.SESSION_DOES_NOT_EXIST;
            }
            else{
                user = session.getUser();
                if(user==null){
                    response = ApiResponse.USER_DOES_NOT_EXIST;
                }
                else{
                    response = ApiResponse.OK;
                }
            }
        } catch (Exception e) {
            response = ApiResponse.UNKNOWN_ERROR;
            //response.setMessage(e.getMessage());
            log.error(e.getMessage());
        }
        switch (response){
            case OK -> {
                GameUserDTO gameUserDTO = GameUserDTO.builder()
                        .email(Optional.ofNullable(user.getEmail()).orElse(""))
                        .username(Optional.ofNullable(user.getUsername()).orElse(""))
                        .city(Optional.ofNullable(user.getCity()).orElse(""))
                        .level(Optional.ofNullable(user.getLevel()).orElse(-1000))
                        .coins(Optional.ofNullable(user.getCoins()).orElse(-1000))
                        .team_id(Optional.ofNullable(user.getTeam()).map(Team::getId).orElse(-1000))
                        .build();


                ApiOk<GameUserDTO> apiOk = ApiResponse.getApiOk(response.getStatusCode(), response.getMessage(), gameUserDTO);
                body = gsonParser.toJson(apiOk);
            }
            default -> throw new ApiException(response);
        }
        return new ResponseEntity<>(body,response.getStatus());
    }

}
