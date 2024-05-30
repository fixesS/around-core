package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameChunk;
import com.around.aroundcore.database.services.GameChunkService;
import com.around.aroundcore.database.services.GameUserService;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.database.services.TeamService;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.exceptions.entity.GameChunkNullException;
import com.around.aroundcore.web.exceptions.entity.GameUserNullException;
import com.around.aroundcore.web.exceptions.entity.SessionNullException;
import com.around.aroundcore.web.exceptions.entity.TeamNullException;
import com.around.aroundcore.web.mappers.GameChunkDTOMapper;
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

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AroundConfig.API_V1_CHUNKS)
@Tag(name="Game Chunk  Controller", description="Controller to get info about controllers")
@SecurityRequirement(name = "JWT")
public class GameChunkController {
    private final SessionService sessionService;
    private final GameUserService userService;
    private final TeamService teamService;
    private final GameChunkService gameChunkService;
    private final GameChunkDTOMapper gameChunkDTOMapper;

    @GetMapping("/{id}")
    @Operation(
            summary = "Gives chunk info by chunk id",
            description = "Allows to get chunk info by chunk id."
    )
    public ResponseEntity<ChunkDTO> getChunkById(@PathVariable @Parameter(description = "Chunk id", example = "123fff") String id){
        ApiResponse response;
        GameChunk chunk;
        ChunkDTO chunkDTO = null;

        try {
            chunk = gameChunkService.findById(id);
            chunkDTO = gameChunkDTOMapper.apply(chunk);
            response = ApiResponse.OK;
        }catch (GameChunkNullException e) {
            response = ApiResponse.CHUNK_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                return new ResponseEntity<>(chunkDTO,response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
    @GetMapping("/my")
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

            chunkDTOList = user.getCapturedChunks().stream().map(gameChunkDTOMapper).toList();
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
    @GetMapping("user/{id}")
    @Operation(
            summary = "Gives all captured chunks by certain user",
            description = "Allows to get all captured chunks in game field by certain user."
    )
    public ResponseEntity<List<ChunkDTO>> getUserChunks(@PathVariable @Parameter(description = "User id", example = "1") Integer id){
        ApiResponse response;
        List<ChunkDTO> chunkDTOList = null;

        try {
            var user = userService.findById(id);

            chunkDTOList = user.getCapturedChunks().stream().map(gameChunkDTOMapper).toList();
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
    @GetMapping("/team/{id}")
    @Operation(
            summary = "Gives all captured chunks by team",
            description = "Allows to get all captured chunks in game field by team."
    )
    public ResponseEntity<List<ChunkDTO>> getTeamChunks(@PathVariable @Parameter(description = "Team id", example = "1") Integer id) {
        ApiResponse response;
        List<ChunkDTO> chunkDTOList = null;

        try {
            var team = teamService.findById(id);
            var chunks = gameChunkService.findAllByOwnerTeam(team);
            chunkDTOList = chunks.stream().map(gameChunkDTOMapper).toList();
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
    @GetMapping("/all")
    @Operation(
            summary = "Gives all captured chunks",
            description = "Allows to get all captured chunks by users. Chunk not in list => chunk does not exist or has not been captured by any user."
    )
    public ResponseEntity<List<ChunkDTO>> getAll(){
        ApiResponse response;

        List<GameChunk> chunkList = gameChunkService.findAll();
        List<ChunkDTO> chunkDTOList = chunkList.stream().map(gameChunkDTOMapper).toList();
        response = ApiResponse.OK;

        return new ResponseEntity<>(chunkDTOList,response.getStatus());
    }
}
