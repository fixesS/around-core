package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.chunk.GameChunk;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.services.*;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.dtos.stat.GameUserStatDTO;
import com.around.aroundcore.web.dtos.stat.TeamStatDTO;
import com.around.aroundcore.core.exceptions.api.entity.NoActiveRoundException;
import com.around.aroundcore.core.exceptions.api.entity.RoundNullException;
import com.around.aroundcore.web.mappers.chunk.GameChunkDTOMapper;
import com.around.aroundcore.web.mappers.stat.TeamStatDTOMapper;
import com.around.aroundcore.web.mappers.stat.UserStatDTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
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
    private final UserStatDTOMapper userStatDTOMapper;
    private final TeamStatDTOMapper teamStatDTOMapper;

    @GetMapping("/{id}")
    @Operation(
            summary = "Gives chunk info by chunk id",
            description = "Allows to get chunk info by chunk id."
    )
    @Transactional
    public ResponseEntity<ChunkDTO> getChunkByIdAndRound(
            @PathVariable @Parameter(description = "Chunk id", example = "123fff") String id,
            @RequestParam("round_id") @Schema(description = "round id (0 - active)") Integer roundId){
        ChunkDTO chunkDTO = gameChunkDTOMapper.apply(getByIdAndRound(id,roundId));

        return ResponseEntity.ok(chunkDTO);
    }
    @GetMapping("/field")
    @Operation(
            summary = "Gives all captured chunks in specific round and city",
            description = "Allows to get all captured chunks by users. Chunk not in list => chunk does not exist or has not been captured by any user."
    )
    @Transactional
    public ResponseEntity<List<ChunkDTO>> getAllByRound(
            @RequestParam("round_id") @Schema(description = "round id (0 - active)") Integer roundId,
            @RequestParam("city_id") Integer cityId ){
        List<GameChunk> chunkList = getChunksByRoundAndCity(roundId,cityId);
        List<ChunkDTO> chunkDTOList = chunkList.stream().map(gameChunkDTOMapper).toList();

        return ResponseEntity.ok(chunkDTOList);
    }
    @GetMapping("/users/me")
    @Operation(
            summary = "Gives all captured chunks by user in specific round",
            description = "MIGHT BE A LONG TIME TO RESPONSE! Allows to get all captured chunks in game field by user. "
    )
    @Transactional
    public ResponseEntity<GameUserStatDTO> getMyChunksStat(){
        UUID sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        var user = session.getUser();
        GameUserStatDTO gameUserStatDTO = userStatDTOMapper.apply(user);

        return ResponseEntity.ok(gameUserStatDTO);
    }
    @GetMapping("/users/me/friends")
    @Operation(
            summary = "Gives all stat of my friends.",
            description = "MIGHT BE A LONG TIME TO RESPONSE! Allows to get stat of my friends."
    )
    @Transactional
    public ResponseEntity<List<GameUserStatDTO>> getMyFriendsChunksStat(){
        UUID sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        var user = session.getUser();
        List<GameUserStatDTO> gameUserStatDTOS = user.getFriends().stream().map(userStatDTOMapper).toList();

        return ResponseEntity.ok(gameUserStatDTOS);
    }
    @GetMapping("/users/{id}")
    @Operation(
            summary = "Gives all captured chunks by certain user in specific round",
            description = "MIGHT BE A LONG TIME TO RESPONSE! Allows to get all captured chunks in game field by certain user."
    )
    @Transactional
    public ResponseEntity<GameUserStatDTO> getUserChunksStatById(@PathVariable Integer id) {
        GameUserStatDTO gameUserStatDTO = userStatDTOMapper.apply(userService.findById(id));

        return ResponseEntity.ok(gameUserStatDTO);
    }@GetMapping("/users/{id}/friends")
    @Operation(
            summary = "Gives all stat of my friends.",
            description = "MIGHT BE A LONG TIME TO RESPONSE! Allows to get stat of my friends."
    )
    @Transactional
    public ResponseEntity<List<GameUserStatDTO>> getMyFriendsChunksStat(@PathVariable Integer id){
        var user = userService.findById(id);
        List<GameUserStatDTO> gameUserStatDTOS = user.getFriends().stream().map(userStatDTOMapper).toList();

        return ResponseEntity.ok(gameUserStatDTOS);
    }
    @GetMapping("/teams/{id}")
    @Operation(
            summary = "Gives stat of team by id.",
            description = "Allows to get stat of team by id."
    )
    @Transactional
    public ResponseEntity<TeamStatDTO> getTeamChunkStatById(@PathVariable @Parameter(description = "team id", example = "1") Integer id) {
        Team team = teamService.findById(id);
        TeamStatDTO teamStatDTO = teamStatDTOMapper.apply(team);

        return ResponseEntity.ok(teamStatDTO);
    }@GetMapping("/teams/all")
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
    private GameChunk getByIdAndRound(String chunkId, Integer roundId) throws NoActiveRoundException, RoundNullException {
        if(roundId == 0 ){
            return gameChunkService.findByIdAndRoundId(chunkId,roundService.getCurrentRound().getId());
        }else{
            roundService.checkById(roundId);
            return gameChunkService.findByIdAndRoundId(chunkId,roundId);
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
}
