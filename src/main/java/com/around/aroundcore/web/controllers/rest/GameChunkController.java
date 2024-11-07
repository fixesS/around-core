package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameChunk;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.services.*;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.exceptions.api.entity.NoActiveRoundException;
import com.around.aroundcore.web.exceptions.api.entity.RoundNullException;
import com.around.aroundcore.web.exceptions.api.entity.TeamNullException;
import com.around.aroundcore.web.exceptions.api.entity.URTNullException;
import com.around.aroundcore.web.mappers.GameChunkDTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping(AroundConfig.API_V1_CHUNKS)
@Tag(name="Game Chunk  Controller", description="Controller to get info about controllers")
@SecurityRequirement(name = "JWT")
public class GameChunkController {
    private final SessionService sessionService;
    private final GameUserService userService;
    private final GameChunkService gameChunkService;
    private final GameChunkDTOMapper gameChunkDTOMapper;
    private final RoundService roundService;
    private final TeamService teamService;
    private final CityService cityService;

    @GetMapping("/{chunkId}")
    @Operation(
            summary = "Gives chunk info by chunk id",
            description = "Allows to get chunk info by chunk id."
    )
    public ResponseEntity<ChunkDTO> getChunkByIdAndRound(@PathVariable @Parameter(description = "Chunk id", example = "123fff") String chunkId,
                                                 @RequestParam("round_id") @Schema(description = "round id (0 - active)") Integer roundId){
        ChunkDTO chunkDTO = gameChunkDTOMapper.apply(getByIdAndRound(chunkId,roundId));

        return ResponseEntity.ok(chunkDTO);
    }
    @GetMapping("/my")
    @Operation(
            summary = "Gives all captured chunks by user in specific round",
            description = "Allows to get all captured chunks in game field by user."
    )
    @Transactional
    public ResponseEntity<List<ChunkDTO>> getMyChunksByRoundAndCity(@RequestParam("round_id") @Schema(description = "round id (0 - active)") Integer roundId,
                                                                    @RequestParam("city_id") Integer cityId){
        UUID sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        var user = session.getUser();
        List<ChunkDTO> chunkDTOList = getChunksByUserAndRoundAndCity(user, roundId, cityId);

        return ResponseEntity.ok(chunkDTOList);
    }
    @GetMapping("user/{userId}")
    @Operation(
            summary = "Gives all captured chunks by certain user in specific round",
            description = "Allows to get all captured chunks in game field by certain user."
    )
    @Transactional
    public ResponseEntity<List<ChunkDTO>> getUserChunksByRound(@PathVariable @Parameter(description = "User id", example = "1") Integer userId,
            @RequestParam("round_id") @Schema(description = "round id (0 - active)") Integer roundId, @RequestParam("city_id") Integer cityId){
        var user = userService.findById(userId);
        List<ChunkDTO> chunkDTOList = getChunksByUserAndRoundAndCity(user,roundId,cityId);

        return ResponseEntity.ok(chunkDTOList);
    }
    @GetMapping("/team/{teamId}")
    @Operation(
            summary = "Gives all captured chunks by team",
            description = "Allows to get all captured chunks in game field by team."
    )
    @Transactional
    public ResponseEntity<List<ChunkDTO>> getTeamChunksByRound(@PathVariable @Parameter(description = "Team id", example = "1") Integer teamId,
                                                        @RequestParam("round_id") @Schema(description = "round id (0 - active)") Integer roundId,
                                                        @RequestParam("city_id") Integer cityId) {
        List<GameChunk> chunks = getChunksByTeamAndRoundAndCity(teamId, roundId, cityId);
        List<ChunkDTO> chunkDTOList = chunks.stream().map(gameChunkDTOMapper).toList();

        return ResponseEntity.ok(chunkDTOList);
    }
    @GetMapping("/all")
    @Operation(
            summary = "Gives all captured chunks in specific round and city",
            description = "Allows to get all captured chunks by users. Chunk not in list => chunk does not exist or has not been captured by any user."
    )
    public ResponseEntity<List<ChunkDTO>> getAllByRound(@RequestParam("round_id") @Schema(description = "round id (0 - active)") Integer roundId,
                                                        @RequestParam("city_id") Integer cityId ){
        List<GameChunk> chunkList = getChunksByRoundAndCity(roundId,cityId);
        List<ChunkDTO> chunkDTOList = chunkList.stream().map(gameChunkDTOMapper).toList();

        return ResponseEntity.ok(chunkDTOList);
    }
    private GameChunk getByIdAndRound(String chunkId, Integer roundId) throws NoActiveRoundException, RoundNullException {
        if(roundId == 0 ){
            return gameChunkService.findByIdAndRoundId(chunkId,roundService.getCurrentRound().getId());
        }else{
            roundService.checkById(roundId);
            return gameChunkService.findByIdAndRoundId(chunkId,roundId);
        }
    }
    private List<GameChunk> getChunksByTeamAndRoundAndCity(Integer teamId, Integer roundId, Integer cityId) throws RoundNullException, NoActiveRoundException, TeamNullException, URTNullException {
        if(roundId == 0){
            return gameChunkService.findAllByRoundAndTeamAndCity(roundService.getCurrentRound(), teamService.findById(teamId), cityService.findById(cityId));
        }else{
            return gameChunkService.findAllByRoundAndTeamAndCity(roundService.getRoundById(roundId), teamService.findById(teamId), cityService.findById(cityId));
        }
    }
    private List<GameChunk> getChunksByRoundAndCity(Integer roundId, Integer cityId) throws RoundNullException, NoActiveRoundException{
        if(roundId == 0 ){
            return gameChunkService.findAllByRoundAndCity(roundService.getCurrentRound().getId(),cityService.findById(cityId).getId());
        }else{
            roundService.checkById(roundId);
            cityService.checkById(cityId);
            return gameChunkService.findAllByRoundAndCity(roundId,cityId);
        }
    }
    private List<ChunkDTO> getChunksByUserAndRoundAndCity(GameUser user, Integer roundId, Integer cityId) throws RoundNullException, NoActiveRoundException{
        if(roundId == 0 ){
            return gameChunkService.findAllByOwnerAndRoundAndCity(user,roundService.getCurrentRound(),cityService.findById(cityId)).stream().map(gameChunkDTOMapper).toList();
        }else{
            return gameChunkService.findAllByOwnerAndRoundAndCity(user,roundService.getRoundById(roundId),cityService.findById(cityId)).stream().map(gameChunkDTOMapper).toList();
        }
    }
}
