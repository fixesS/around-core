package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.user.GameUser;
import com.around.aroundcore.database.services.*;
import com.around.aroundcore.web.dtos.stat.GameUserStatDTO;
import com.around.aroundcore.web.mappers.stat.UserStatDTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AroundConfig.API_V1_STATISTIC)
@Tag(name="Statistic Controller", description="Controller to get all statistic about users,teams,chunks(cells)")
@SecurityRequirement(name = "JWT")
public class StatisticController {
    private final RoundService roundService;
    private final GameUserService userService;
    private final UserStatDTOMapper userStatDTOMapper;


    @GetMapping("/user/top")
    @Operation(
            summary = "Gives top users by rounds, users, limit",
            description = "sorting_by_chunks_all - sorting by chunks all (captured chunks in current round for all cities) if true; sorting ny chunks that owned by users right now if false."
    )
    @Transactional
    public ResponseEntity<List<GameUserStatDTO>> getTopUsersStat(
            @RequestParam(value = "rounds",required = false,defaultValue = "") List<Integer> rounds,
            @RequestParam(value = "users",required = false,defaultValue = "") List<Integer> users,
            @RequestParam("limit") @Schema(description = "min 0, max 100") Integer limit,
            @RequestParam(value = "sorting_by_chunks_all",required = false) Boolean sortingByChunksAll) {

        List<GameUser> topUsers = userService.getTopUsersByChunks(
                // find for all by ids to exclude cases of missing entities in runtime
                rounds.stream().map(roundService::getRoundById).toList(),
                users.stream().map(userService::findById).toList(),
                limit,
                sortingByChunksAll);
        List<GameUserStatDTO> gameUserStatDTOS = topUsers.stream().map(userStatDTOMapper).toList();

        return ResponseEntity.ok(gameUserStatDTOS);
    }

}
