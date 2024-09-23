package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.services.*;
import com.around.aroundcore.web.dtos.TeamStatDTO;
import com.around.aroundcore.web.dtos.UserStatDTO;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.exceptions.entity.GameUserNullException;
import com.around.aroundcore.web.exceptions.entity.SessionNullException;
import com.around.aroundcore.web.exceptions.entity.TeamNullException;
import com.around.aroundcore.web.mappers.TeamStatDTOMapper;
import com.around.aroundcore.web.mappers.UserStatDTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AroundConfig.API_V1_STATISTIC)
@Tag(name="Statistic Controller", description="Controller to get all statistic about users,teams,chunks(cells)")
@SecurityRequirement(name = "JWT")
public class StatisticController {
    private final SessionService sessionService;
    private final GameUserService userService;
    private final TeamService teamService;
    private final UserStatDTOMapper userStatDTOMapper;
    private final TeamStatDTOMapper teamStatDTOMapper;

    @GetMapping("/user/me/friends")
    @Operation(
            summary = "Gives all stat of my friends.",
            description = "Allows to get stat of my friends."
    )
    public ResponseEntity<List<UserStatDTO>> getMyFriendsStat(){
        ApiResponse response;
        List<UserStatDTO> userStatDTOS = new ArrayList<>();

        try {
            UUID sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var session = sessionService.findByUuid(sessionUuid);
            var user = session.getUser();
            userStatDTOS = user.getFriends().stream().map(userStatDTOMapper).toList();

            response = ApiResponse.OK;
        }catch (SessionNullException e) {
            response = ApiResponse.SESSION_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (GameUserNullException e) {
            response = ApiResponse.USER_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (TeamNullException e) {
            response = ApiResponse.USER_HAS_NO_TEAM_IN_ROUND;
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                return new ResponseEntity<>(userStatDTOS,response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
    @GetMapping("/team/all")
    @Operation(
            summary = "Gives all stat of all teams for every round.",
            description = "Allows to get stat of all teams."
    )
    public ResponseEntity<List<TeamStatDTO>> getTeamsStat() {
        ApiResponse response;
        List<TeamStatDTO> teamStatDTOS = new ArrayList<>();

        try {
            List<Team> teams = teamService.findAll();
            teamStatDTOS = teams.stream().map(teamStatDTOMapper).toList();
            response = ApiResponse.OK;
        } catch (SessionNullException e) {
            response = ApiResponse.SESSION_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (TeamNullException e) {
            response = ApiResponse.TEAM_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }

        switch (response) {
            case OK -> {
                return new ResponseEntity<>(teamStatDTOS, response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
    @GetMapping("/team/{id}")
    @Operation(
            summary = "Gives stat of team by id.",
            description = "Allows to get stat of team by id."
    )
    public ResponseEntity<TeamStatDTO> getTeamStatById(@PathVariable @Parameter(description = "team id", example = "1") Integer id) {
        ApiResponse response;
        TeamStatDTO teamStatDTO = null;

        try {
            Team team = teamService.findById(id);
            teamStatDTO = teamStatDTOMapper.apply(team);
            response = ApiResponse.OK;
        } catch (SessionNullException e) {
            response = ApiResponse.SESSION_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (TeamNullException e) {
            response = ApiResponse.TEAM_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }

        switch (response) {
            case OK -> {
                return new ResponseEntity<>(teamStatDTO, response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
    @GetMapping("/user/{id}")
    @Operation(
            summary = "Gives stat of user by id in active round.",
            description = "Allows to get stat of user by id.."
    )
    public ResponseEntity<UserStatDTO> getUserStatById(@PathVariable @Parameter(description = "user id", example = "1") Integer id) {
        ApiResponse response;
        UserStatDTO userStatDTO = null;

        try {
            userStatDTO = userStatDTOMapper.apply(userService.findById(id));

            response = ApiResponse.OK;
        } catch (SessionNullException e) {
            response = ApiResponse.SESSION_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (TeamNullException e) {
            response = ApiResponse.USER_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }

        switch (response) {
            case OK -> {
                return new ResponseEntity<>(userStatDTO, response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
    @GetMapping("/user/me")
    @Operation(
            summary = "Gives all my stat in all rounds.",
            description = "Allows to get my stat."
    )
    public ResponseEntity<UserStatDTO> getMyStat() {
        ApiResponse response;
        UserStatDTO userStatDTO = null;

        try {
            UUID sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var session = sessionService.findByUuid(sessionUuid);
            var user = session.getUser();
            userStatDTO = userStatDTOMapper.apply(user);

            response = ApiResponse.OK;
        } catch (SessionNullException e) {
            response = ApiResponse.SESSION_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (TeamNullException e) {
            response = ApiResponse.TEAM_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }

        switch (response) {
            case OK -> {
                return new ResponseEntity<>(userStatDTO, response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
    @GetMapping("/user/top")
    @Operation(
            summary = "Gives top 50 users in all rounds",
            description = "Allows to get top 50 users."
    )
    public ResponseEntity<List<UserStatDTO>> getTopUsersStat() {
        ApiResponse response;
        List<UserStatDTO> userStatDTOS = new ArrayList<>();

        try {
            List<GameUser> topUsers = userService.getTopAll();
            userStatDTOS = topUsers.stream().map(userStatDTOMapper).toList();

            response = ApiResponse.OK;
        } catch (SessionNullException e) {
            response = ApiResponse.SESSION_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (TeamNullException e) {
            response = ApiResponse.TEAM_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }

        switch (response) {
            case OK -> {
                return new ResponseEntity<>(userStatDTOS, response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }

}
