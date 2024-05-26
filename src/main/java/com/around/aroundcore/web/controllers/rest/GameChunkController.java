package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameChunk;
import com.around.aroundcore.database.services.GameChunkService;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.exceptions.entity.GameChunkNullException;
import com.around.aroundcore.web.mappers.GameChunkDTOMapper;
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
@RequestMapping(AroundConfig.API_V1_CHUNKS)
@Tag(name="Game Chunk  Controller", description="Controller to get info about controllers")
@SecurityRequirement(name = "JWT")
public class GameChunkController {
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
