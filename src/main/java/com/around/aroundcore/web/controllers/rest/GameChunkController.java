package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameChunk;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.UserRoundTeam;
import com.around.aroundcore.database.services.*;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.exceptions.entity.*;
import com.around.aroundcore.web.mappers.GameChunkDTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public ResponseEntity<List<ChunkDTO>> getMyChunksByRound(@RequestParam("round_id") @Schema(description = "round id (0 - active)") Integer roundId){
        UUID sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        var user = session.getUser();
        List<ChunkDTO> chunkDTOList = getFilteredChunkDTOs(user, roundId);

        return ResponseEntity.ok(chunkDTOList);
    }
    @GetMapping("user/{userId}")
    @Operation(
            summary = "Gives all captured chunks by certain user in specific round",
            description = "Allows to get all captured chunks in game field by certain user."
    )
    public ResponseEntity<List<ChunkDTO>> getUserChunksByRound(@PathVariable @Parameter(description = "User id", example = "1") Integer userId,
            @RequestParam("round_id") @Schema(description = "round id (0 - active)") Integer roundId ){
        var user = userService.findById(userId);
        List<ChunkDTO> chunkDTOList = getFilteredChunkDTOs(user,roundId);

        return ResponseEntity.ok(chunkDTOList);
    }
    @GetMapping("/team/{teamId}")
    @Operation(
            summary = "Gives all captured chunks by team",
            description = "Allows to get all captured chunks in game field by team."
    )
    public ResponseEntity<List<ChunkDTO>> getTeamChunksByRound(@PathVariable @Parameter(description = "Team id", example = "1") Integer teamId,
                                                        @RequestParam("round_id") @Schema(description = "round id (0 - active)") Integer roundId ) {
        List<GameChunk> chunks = getChunksByTeamAndRound(teamId, roundId);
        List<ChunkDTO> chunkDTOList = chunks.stream().map(gameChunkDTOMapper).toList();

        return ResponseEntity.ok(chunkDTOList);
    }
    @GetMapping("/all")
    @Operation(
            summary = "Gives all captured chunks in specific round",
            description = "Allows to get all captured chunks by users. Chunk not in list => chunk does not exist or has not been captured by any user."
    )
    public ResponseEntity<List<ChunkDTO>> getAllByRound(@RequestParam("round_id") @Schema(description = "round id (0 - active)") Integer roundId){
        List<GameChunk> chunkList = getChunksByRound(roundId);
        List<ChunkDTO> chunkDTOList = chunkList.stream().map(gameChunkDTOMapper).toList();

        return ResponseEntity.ok(chunkDTOList);
    }
    private GameChunk getByIdAndRound(String chunkId, Integer roundId) throws NoActiveRoundException, RoundNullException{
        if(roundId == 0 ){
            return gameChunkService.findByIdAndRoundId(chunkId,roundService.getCurrentRound().getId());
        }else{
            roundService.checkById(roundId);
            return gameChunkService.findByIdAndRoundId(chunkId,roundId);
        }
    }
    private List<GameChunk> getChunksByTeamAndRound(Integer teamId, Integer roundId) throws RoundNullException, NoActiveRoundException,TeamNullException, URTNullException{
        UserRoundTeam urt;
        if(roundId == 0){
            urt = roundService.getUserRoundTeamByTeamInCurrentRound(teamId);
        }else{
            urt = roundService.getUserRoundTeamByTeamInRound(teamId, roundId);
        }
        return gameChunkService.findAllByUserRoundTeam(urt);
    }
    private List<GameChunk> getChunksByRound(Integer roundId) throws RoundNullException, NoActiveRoundException{
        if(roundId == 0 ){
            return gameChunkService.findAllByRound(roundService.getCurrentRound().getId());
        }else{
            roundService.checkById(roundId);
            return gameChunkService.findAllByRound(roundId);
        }
    }
    private List<ChunkDTO> getFilteredChunkDTOs(GameUser user, Integer roundId) throws RoundNullException, NoActiveRoundException{
        if(roundId == 0 ){
            return user.getCapturedChunks(roundService.getCurrentRound().getId()).stream().map(gameChunkDTOMapper).toList();
        }else{
            roundService.checkById(roundId);
            return user.getCapturedChunks(roundId).stream().map(gameChunkDTOMapper).toList();
        }
    }
}
