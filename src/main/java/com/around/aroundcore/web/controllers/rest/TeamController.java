package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.services.TeamService;
import com.around.aroundcore.web.dtos.TeamDTO;
import com.around.aroundcore.web.mappers.TeamDTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AroundConfig.API_V1_TEAM)
@Tag(name="Team Controller", description="Allows to get info about teams")
@SecurityRequirement(name = "JWT")
public class TeamController {
    private final TeamService teamService;
    private final TeamDTOMapper teamDTOMapper;
    @GetMapping("/{id}")
    @Operation(
            summary = "Gives team info by id in current round",
            description = "Allows to get info about team by id in current round."
    )
    @Transactional
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable @Parameter(description = "team id") Integer id){
        var team = teamService.findById(id);
        TeamDTO teamDTO = teamDTOMapper.apply(team);

        return ResponseEntity.ok(teamDTO);
    }

    @GetMapping("/all")
    @Operation(
            summary = "Gives all teams info in current round",
            description = "Allows to get info about all teams in current round."
    )
    @Transactional
    public ResponseEntity<List<TeamDTO>> getAllTeams(){
        List<Team> teamList = teamService.findAll();
        List<TeamDTO> teamDTOList = teamList.stream().map(teamDTOMapper).toList();

        return ResponseEntity.ok(teamDTOList);
    }
}
