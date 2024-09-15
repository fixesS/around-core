package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameChunk;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Round;
import com.around.aroundcore.database.models.UserRoundTeam;
import com.around.aroundcore.database.services.*;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.exceptions.entity.*;
import com.around.aroundcore.web.mappers.GameChunkDTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
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
        ApiResponse response;
        GameChunk chunk;
        ChunkDTO chunkDTO = null;

        try {
            chunk = getByIdAndRound(chunkId,roundId);
            chunkDTO = gameChunkDTOMapper.apply(chunk);
            response = ApiResponse.OK;
        }catch (GameChunkNullException e) {
            response = ApiResponse.CHUNK_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }catch (NoActiveRoundException e){
            response = ApiResponse.NO_ACTIVE_ROUND;
            log.error(e.getMessage());
        }catch (RoundNullException  e) {
            response = ApiResponse.ROUND_DOES_NOT_EXIST;
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
            summary = "Gives all captured chunks by user in specific round",
            description = "Allows to get all captured chunks in game field by user."
    )
    public ResponseEntity<List<ChunkDTO>> getMyChunksByRound(@RequestParam("round_id") @Schema(description = "round id (0 - active)") Integer roundId){
        ApiResponse response;
        List<ChunkDTO> chunkDTOList = null;

        UUID sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            var session = sessionService.findByUuid(sessionUuid);
            var user = session.getUser();

            chunkDTOList = getFilteredChunkDTOs(user, roundId);
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
        }catch (NoActiveRoundException e){
            response = ApiResponse.NO_ACTIVE_ROUND;
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                return new ResponseEntity<>(chunkDTOList,response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
    @GetMapping("user/{userId}")
    @Operation(
            summary = "Gives all captured chunks by certain user in specific round",
            description = "Allows to get all captured chunks in game field by certain user."
    )
    public ResponseEntity<List<ChunkDTO>> getUserChunksByRound(@PathVariable @Parameter(description = "User id", example = "1") Integer userId,
            @RequestParam("round_id") @Schema(description = "round id (0 - active)") Integer roundId ){
        ApiResponse response;
        List<ChunkDTO> chunkDTOList = null;

        try {
            var user = userService.findById(userId);

            chunkDTOList = getFilteredChunkDTOs(user,roundId);
            response = ApiResponse.OK;
        } catch (GameUserNullException e) {
            response = ApiResponse.USER_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (TeamNullException e) {
            response = ApiResponse.USER_HAS_NO_TEAM;
            log.error(e.getMessage());
        } catch (NoActiveRoundException e){
            response = ApiResponse.NO_ACTIVE_ROUND;
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                return new ResponseEntity<>(chunkDTOList,response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
    @GetMapping("/team/{teamId}")
    @Operation(
            summary = "Gives all captured chunks by team",
            description = "Allows to get all captured chunks in game field by team."
    )
    public ResponseEntity<List<ChunkDTO>> getTeamChunksByRound(@PathVariable @Parameter(description = "Team id", example = "1") Integer teamId,
                                                        @RequestParam("round_id") @Schema(description = "round id (0 - active)") Integer roundId ) {
        ApiResponse response;
        List<ChunkDTO> chunkDTOList = null;

        try {
            var chunks = getChunksByTeamAndRound(teamId, roundId);
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
        } catch (RoundNullException  e) {
            response = ApiResponse.ROUND_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (NoActiveRoundException  e) {
            response = ApiResponse.NO_ACTIVE_ROUND;
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
            summary = "Gives all captured chunks in specific round",
            description = "Allows to get all captured chunks by users. Chunk not in list => chunk does not exist or has not been captured by any user."
    )
    public ResponseEntity<List<ChunkDTO>> getAllByRound(@RequestParam("round_id") @Schema(description = "round id (0 - active)") Integer roundId){
        ApiResponse response;

        List<GameChunk> chunkList = getChunksByRound(roundId);
        List<ChunkDTO> chunkDTOList = chunkList.stream().map(gameChunkDTOMapper).toList();
        response = ApiResponse.OK;

        return new ResponseEntity<>(chunkDTOList,response.getStatus());
    }
    private GameChunk getByIdAndRound(String chunkId, Integer roundId) throws NoActiveRoundException, RoundNullException{
        if(roundId == 0 ){
            return gameChunkService.findByIdAndRoundId(chunkId,roundService.getCurrentRound().getId());
        }else{
            roundService.checkIfExistById(roundId);
            return gameChunkService.findByIdAndRoundId(chunkId,roundId);
        }
    }
    private List<GameChunk> getChunksByTeamAndRound(Integer teamId, Integer roundId) throws RoundNullException, NoActiveRoundException{
        UserRoundTeam urt;
        if(roundId == 0){
            urt = roundService.getUserRoundTeamByTeamInCurrentRound(teamId);
        }else{
            roundService.checkIfExistById(roundId);
            urt = roundService.getUserRoundTeamByTeamInRound(teamId, roundId);
        }
        return gameChunkService.findAllByUserRoundTeam(urt);
    }
    private List<GameChunk> getChunksByRound(Integer roundId) throws GameChunkNullException, NoActiveRoundException{
        if(roundId == 0 ){
            return gameChunkService.findAllByRound(roundService.getCurrentRound().getId());
        }else{
            roundService.checkIfExistById(roundId);
            return gameChunkService.findAllByRound(roundId);
        }
    }
    private List<ChunkDTO> getFilteredChunkDTOs(GameUser user, Integer roundId) throws NoActiveRoundException{
        if(roundId == 0 ){
            return user.getCapturedChunks(roundService.getCurrentRound().getId()).stream().map(gameChunkDTOMapper).toList();
        }else{
            roundService.checkIfExistById(roundId);
            return user.getCapturedChunks(roundId).stream().map(gameChunkDTOMapper).toList();
        }
    }
}
