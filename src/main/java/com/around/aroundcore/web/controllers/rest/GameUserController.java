package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.Skill;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.web.dtos.GameUserDTO;
import com.around.aroundcore.web.dtos.SkillDTO;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.exceptions.entity.GameUserNullException;
import com.around.aroundcore.web.exceptions.entity.SessionNullException;
import com.around.aroundcore.web.exceptions.entity.SkillNullException;
import com.around.aroundcore.web.mappers.GameUserDTOMapper;
import com.around.aroundcore.web.mappers.SkillDTOMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    private final SessionService sessionService;
    private final GameUserDTOMapper gameUserDTOMapper;
    private final SkillDTOMapper skillDTOMapper;

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
            gameUserDTO = gameUserDTOMapper.apply(user);
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
  
    @GetMapping("/me/friends")
    @Operation(
            summary = "Gives friends",
            description = "Allows get info about all friends of user."
    )
    public ResponseEntity<List<GameUserDTO>> getMyFriends() {
        ApiResponse response;
        List<GameUserDTO> friends = null;

        try {
            var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var session = sessionService.findByUuid(sessionUuid);
            var user = session.getUser();
            friends = user.getFriends().stream().map(gameUserDTOMapper).toList();
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
    @GetMapping("/me/skills")
    @Operation(
            summary = "Gives friends",
            description = "Allows get info about all friends of user."
    )
    public ResponseEntity<List<SkillDTO>> getMySkills() {
        ApiResponse response;
        List<SkillDTO> skillDTOS = null;

        try {
            var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var session = sessionService.findByUuid(sessionUuid);
            var user = session.getUser();
            List<Skill> skills = user.getUserSkills().stream().map(gameUserSkill -> gameUserSkill.getGameUserSkillEmbedded().getSkill()).toList();
            skillDTOS = skills.stream().map(skillDTOMapper).toList();
            response = ApiResponse.OK;
        } catch (SessionNullException e) {
            response = ApiResponse.SESSION_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (GameUserNullException e) {
            response = ApiResponse.USER_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (SkillNullException e){
            response = ApiResponse.SKILL_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }

        switch (response) {
            case OK -> {
                return new ResponseEntity<>(skillDTOS,response.getStatus());

            }
            default -> throw new ApiException(response);
        }
    }
}
