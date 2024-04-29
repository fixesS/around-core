package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.services.TeamService;
import com.around.aroundcore.web.dtos.TeamDTO;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.exceptions.entity.TeamNullException;
import com.around.aroundcore.web.mappers.TeamDTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
            summary = "Gives team info by id",
            description = "Allows to get info about team by id."
    )
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable @Parameter(description = "team id") Integer id){
        ApiResponse response;
        TeamDTO teamDTO = null;
        try{
            var team = teamService.findById(id);
            teamDTO = teamDTOMapper.apply(team);
            response = ApiResponse.OK;
        }catch (TeamNullException e) {
            response = ApiResponse.TEAM_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                return new ResponseEntity<>(teamDTO,response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }

    @GetMapping("/all")
    @Operation(
            summary = "Gives all teams info",
            description = "Allows to get info about all created teams."
    )
    public ResponseEntity<List<TeamDTO>> getAllTeams(){
        ApiResponse response;

        List<Team> teamList = teamService.findAll();
        List<TeamDTO> teamDTOList = teamList.stream().map(teamDTOMapper).toList();
        response = ApiResponse.OK;

        return new ResponseEntity<>(teamDTOList,response.getStatus());
    }
}
