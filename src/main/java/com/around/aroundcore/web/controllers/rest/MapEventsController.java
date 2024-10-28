package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.MapEvent;
import com.around.aroundcore.database.services.MapEventService;
import com.around.aroundcore.web.dtos.MapEventDTO;
import com.around.aroundcore.web.mappers.MapEventDTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AroundConfig.API_V1_EVENTS)
@Tag(name="MapEvents", description="Allows to get info about events on map")
@SecurityRequirement(name = "JWT")
public class MapEventsController {

    private final MapEventService mapEventService;
    private final MapEventDTOMapper mapEventDTOMapper;
    @GetMapping("/all")
    @Operation(
            summary = "Gives all map-events in city",
            description = "Allows to get all map-events in city."
    )
    public ResponseEntity<List<MapEventDTO>> getAllEventsInCity(@RequestParam("city_id") Integer cityId) {
        List<MapEvent> events = mapEventService.findAllVerifiedInCity(cityId);
        List<MapEventDTO> eventDTOS = events.stream().map(mapEventDTOMapper).toList();

        return ResponseEntity.ok(eventDTOS);
    }
}
