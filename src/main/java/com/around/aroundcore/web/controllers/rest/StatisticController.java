package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.services.*;
import com.around.aroundcore.web.dtos.TeamStatDTO;
import com.around.aroundcore.web.dtos.GameUserStatDTO;
import com.around.aroundcore.web.mappers.TeamStatDTOMapper;
import com.around.aroundcore.web.mappers.UserStatDTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    @Transactional
    public ResponseEntity<List<GameUserStatDTO>> getMyFriendsStat(){
        UUID sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        var user = session.getUser();
        List<GameUserStatDTO> gameUserStatDTOS = user.getFriends().stream().map(userStatDTOMapper).toList();

        return ResponseEntity.ok(gameUserStatDTOS);
    }
    @GetMapping("/team/all")
    @Operation(
            summary = "Gives all stat of all teams for every round.",
            description = "Allows to get stat of all teams."
    )
    @Transactional
    public ResponseEntity<List<TeamStatDTO>> getTeamsStat() {

        List<Team> teams = teamService.findAll();
        List<TeamStatDTO> teamStatDTOS = teams.stream().map(teamStatDTOMapper).toList();

        return ResponseEntity.ok(teamStatDTOS);
    }
    @GetMapping("/team/{id}")
    @Operation(
            summary = "Gives stat of team by id.",
            description = "Allows to get stat of team by id."
    )
    @Transactional
    public ResponseEntity<TeamStatDTO> getTeamStatById(@PathVariable @Parameter(description = "team id", example = "1") Integer id) {
        Team team = teamService.findById(id);
        TeamStatDTO teamStatDTO = teamStatDTOMapper.apply(team);

        return ResponseEntity.ok(teamStatDTO);
    }
    @GetMapping("/user/{id}")
    @Operation(
            summary = "Gives stat of user by id in active round.",
            description = "Allows to get stat of user by id.."
    )
    @Transactional
    public ResponseEntity<GameUserStatDTO> getUserStatById(@PathVariable @Parameter(description = "user id", example = "1") Integer id) {
        GameUserStatDTO gameUserStatDTO = userStatDTOMapper.apply(userService.findById(id));

        return ResponseEntity.ok(gameUserStatDTO);
    }
    @GetMapping("/user/me")
    @Operation(
            summary = "Gives all my stat in all rounds.",
            description = "Allows to get my stat."
    )
    @Transactional
    public ResponseEntity<GameUserStatDTO> getMyStat() {
        UUID sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        var user = session.getUser();
        GameUserStatDTO gameUserStatDTO = userStatDTOMapper.apply(user);

        return ResponseEntity.ok(gameUserStatDTO);
    }
    @GetMapping("/user/top")
    @Operation(
            summary = "Gives top 50 users in all rounds",
            description = "Allows to get top 50 users."
    )
    @Transactional
    public ResponseEntity<List<GameUserStatDTO>> getTopUsersStat() {
        List<GameUser> topUsers = userService.getTopAll();
        List<GameUserStatDTO> gameUserStatDTOS = topUsers.stream().map(userStatDTOMapper).toList();

        return ResponseEntity.ok(gameUserStatDTOS);
    }

}
