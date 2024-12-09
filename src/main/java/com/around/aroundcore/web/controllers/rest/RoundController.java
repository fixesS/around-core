package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.services.round.RoundService;
import com.around.aroundcore.web.dtos.round.RoundDTO;
import com.around.aroundcore.web.mappers.RoundDTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AroundConfig.API_V1_ROUND)
@Tag(name="Round controller", description="Handles request about rounds")
public class RoundController {
    private final RoundService roundService;
    private final RoundDTOMapper roundDTOMapper;

    @GetMapping("/current")
    @Operation(
            summary = "Gives current active round",
            description = "Allows to get info about current active round."
    )
    public ResponseEntity<RoundDTO> getCurrentRound() {
        RoundDTO roundDTO = roundDTOMapper.apply(roundService.getCurrentRound());

        return ResponseEntity.ok(roundDTO);
    }
    @GetMapping("/{id}")
    @Operation(
            summary = "Gives round by id",
            description = "Allows to get info about round by id."
    )
    public ResponseEntity<RoundDTO> getRoundById(@PathVariable("id") Integer id) {
        RoundDTO roundDTO = roundDTOMapper.apply(roundService.getRoundById(id));

        return ResponseEntity.ok(roundDTO);
    }
}
