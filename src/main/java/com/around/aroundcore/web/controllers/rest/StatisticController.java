package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameChunk;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Session;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.services.GameChunkService;
import com.around.aroundcore.database.services.GameUserService;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.database.services.TeamService;
import com.around.aroundcore.web.dto.ChunkDTO;
import com.around.aroundcore.web.dto.GameUserDTO;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.exceptions.entity.GameUserNullException;
import com.around.aroundcore.web.exceptions.entity.SessionNullException;
import com.around.aroundcore.web.exceptions.entity.TeamNullException;
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

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AroundConfig.API_V1_STATISTIC)
@Tag(name="Statistic Controller", description="Controller to get all statistic about users,teams,chunks(cells)")
@SecurityRequirement(name = "JWT")
public class StatisticController {
    private SessionService sessionService;
    private GameUserService userService;
    private GameChunkService gameChunkService;
    private TeamService teamService;
    @GetMapping("chunks/my")
    @Operation(
            summary = "Gives all captured chunks by user",
            description = "Allows to get all captured chunks in game field by user."
    )
    public ResponseEntity<List<ChunkDTO>> getMyChunks(){
        ApiResponse response;
        List<ChunkDTO> chunkDTOList = null;

        UUID sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            var session = sessionService.findByUuid(sessionUuid);
            var user = session.getUser();

            chunkDTOList = user.getCapturedChunks().stream().map(gameChunk -> new ChunkDTO(gameChunk.getId(), gameChunk.getOwner().getTeam().getId())).toList();
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
                return new ResponseEntity<>(chunkDTOList,response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
    @GetMapping("chunks/user/{id}")
    @Operation(
            summary = "Gives all captured chunks by certain user",
            description = "Allows to get all captured chunks in game field by certain user."
    )
    public ResponseEntity<List<ChunkDTO>> getUserChunks(@PathVariable @Parameter(description = "User id", example = "1") Integer id){
        ApiResponse response;
        List<ChunkDTO> chunkDTOList = null;

        try {
            var user = userService.findById(id);

            chunkDTOList = user.getCapturedChunks().stream().map(gameChunk -> new ChunkDTO(gameChunk.getId(), gameChunk.getOwner().getTeam().getId())).toList();
            response = ApiResponse.OK;
        } catch (GameUserNullException e) {
            response = ApiResponse.USER_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (TeamNullException e) {
            response = ApiResponse.USER_HAS_NO_TEAM;
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                return new ResponseEntity<>(chunkDTOList,response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
    @GetMapping("/chunks/team/{id}")
    public ResponseEntity<List<ChunkDTO>> getTeamChunks(@PathVariable @Parameter(description = "Team id", example = "1") Integer id) {
        ApiResponse response;
        List<ChunkDTO> chunkDTOList = null;

        try {
            var team = teamService.findById(id);
            var chunks = gameChunkService.findAllByOwnerTeam(team);
            chunkDTOList = chunks.stream().map(
                    gameChunk -> ChunkDTO.builder()
                            .id(gameChunk.getId())
                            .team_id(team.getId())
                            .build()
                    ).toList();
            response = ApiResponse.OK;
        } catch (SessionNullException e) {
            response = ApiResponse.SESSION_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (GameUserNullException e) {
            response = ApiResponse.USER_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (TeamNullException e) {
            response = ApiResponse.USER_HAS_NO_TEAM;
            log.error(e.getMessage());
        }

        switch (response) {
            case OK -> {
                return new ResponseEntity<>(chunkDTOList,response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
}
