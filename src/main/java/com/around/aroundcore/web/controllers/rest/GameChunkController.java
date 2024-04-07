package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameChunk;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Session;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.services.GameChunkService;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.web.dto.ApiOk;
import com.around.aroundcore.web.dto.ChunkDTO;
import com.around.aroundcore.web.dto.GameUserDTO;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.exceptions.entity.GameChunkNullException;
import com.around.aroundcore.web.exceptions.entity.GameUserNullException;
import com.around.aroundcore.web.exceptions.entity.SessionNullException;
import com.around.aroundcore.web.gson.GsonParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AroundConfig.API_V1_CHUNKS)
@Tag(name="Game Chunk  Controller", description="Controller to get info about controllers")
@SecurityRequirement(name = "JWT")
public class GameChunkController {
    private SessionService sessionService;
    private GameChunkService gameChunkService;

    @GetMapping("/all")
    @Operation(
            summary = "Gives all captured chunks",
            description = "Allows to get all captured chunks by users. Chunk not in list => chunk does not exist or has not been captured by any user."
    )
    public ResponseEntity<List<ChunkDTO>> handleGetAll(){
        ApiResponse response;

        List<GameChunk> chunkList = gameChunkService.findAll();
        List<ChunkDTO> chunkDTOList = chunkList.stream().map(gameChunk -> new ChunkDTO(gameChunk.getId(), gameChunk.getOwner().getTeam().getId())).toList();
        response = ApiResponse.OK;

        switch (response){
            case OK -> {
                return new ResponseEntity<>(chunkDTOList,response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
    @GetMapping("/my")
    @Operation(
            summary = "Gives all captured chunks by certain user",
            description = "Allows to get all captured chunks in game field by certain user."
    )
    public ResponseEntity<List<ChunkDTO>> handleGetMy(){
        ApiResponse response;
        Session session = null;
        GameUser user = null;
        List<ChunkDTO> chunkDTOList = null;

        UUID sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            session = sessionService.findByUuid(sessionUuid);
            user = session.getUser();

            List<GameChunk> chunkList = gameChunkService.findAllByOwner(user);
            chunkDTOList = chunkList.stream().map(gameChunk -> new ChunkDTO(gameChunk.getId(), gameChunk.getOwner().getTeam().getId())).toList();
            response = ApiResponse.OK;
        }catch (SessionNullException e) {
            response = ApiResponse.SESSION_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (GameUserNullException e) {
            response = ApiResponse.USER_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (Exception e) {
            response = ApiResponse.UNKNOWN_ERROR;
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                return new ResponseEntity<>(chunkDTOList,response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
    @GetMapping("/{id}")
    @Operation(
            summary = "Gives chunk info by chunk id",
            description = "Allows to get chunk info by chunk id."
    )
    public ResponseEntity<ChunkDTO> handleGetChunkById(@PathVariable @Parameter(description = "Chunk id") String id){
        ApiResponse response;
        GameChunk chunk;
        ChunkDTO chunkDTO = null;


        try {
            chunk = gameChunkService.findById(id);

            chunkDTO = ChunkDTO.builder()
                    .id(id)
                    .team_id(chunk.getOwner().getTeam().getId())
                    .build();
            response = ApiResponse.OK;
        }catch (GameChunkNullException e) {
            response = ApiResponse.CHUNK_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (Exception e) {
            response = ApiResponse.UNKNOWN_ERROR;
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                return new ResponseEntity<>(chunkDTO,response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }

}
