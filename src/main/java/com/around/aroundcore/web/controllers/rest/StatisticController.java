package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.services.GameChunkService;
import com.around.aroundcore.database.services.GameUserService;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.database.services.TeamService;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.dtos.GameUserDTO;
import com.around.aroundcore.web.dtos.TeamStatDTO;
import com.around.aroundcore.web.dtos.UserStatDTO;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.exceptions.entity.GameUserNullException;
import com.around.aroundcore.web.exceptions.entity.SessionNullException;
import com.around.aroundcore.web.exceptions.entity.TeamNullException;
import com.around.aroundcore.web.mappers.GameChunkDTOMapper;
import com.around.aroundcore.web.mappers.UserStatDTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AroundConfig.API_V1_STATISTIC)
@Tag(name="Statistic Controller", description="Controller to get all statistic about users,teams,chunks(cells)")
@SecurityRequirement(name = "JWT")
public class    StatisticController {
    private final SessionService sessionService;
    private final GameUserService userService;
    private final TeamService teamService;
    private final UserStatDTOMapper userStatDTOMapper;

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
            response = ApiResponse.USER_HAS_NO_TEAM;
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
            summary = "Gives all stat of all teams.",
            description = "Allows to get stat of all teams."
    )
    public ResponseEntity<List<TeamStatDTO>> getTeamsStat() {
        ApiResponse response;
        List<TeamStatDTO> teamStatDTOS = new ArrayList<>();

        try {
            List<Team> teams = teamService.findAll();
            for(Team team : teams){
                AtomicReference<Integer> numOfChunks = new AtomicReference<>(0);
                team.getMembers().forEach(member -> numOfChunks.set(numOfChunks.get() + member.getCapturedChunks().size()));
                teamStatDTOS.add(TeamStatDTO.builder()
                        .id(team.getId())
                        .color(team.getColor())
                        .number(numOfChunks.get())
                        .build());
            }

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
            AtomicReference<Integer> numOfChunks = new AtomicReference<>(0);
            team.getMembers().forEach(member -> numOfChunks.set(numOfChunks.get() + member.getCapturedChunks().size()));
            teamStatDTO = TeamStatDTO.builder()
                    .id(team.getId())
                    .color(team.getColor())
                    .number(numOfChunks.get())
                    .build();

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
            summary = "Gives stat of user by id.",
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
            summary = "Gives all my stat.",
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
            summary = "Gives top 50 users.",
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
