package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.services.CityService;
import com.around.aroundcore.web.dtos.CityDTO;
import com.around.aroundcore.web.mappers.CityDTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
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
@RequestMapping(AroundConfig.API_V1_CITY)
@Tag(name="Cities controller", description="Controller to get info about city.")
public class CityController {
    private final CityService cityService;
    private final CityDTOMapper cityDTOMapper;

    @GetMapping("/{id}")
    @Operation(
            summary = "Gives info about city by id",
            description = "Allows to get info about city by id."
    )
    @Transactional
    public ResponseEntity<CityDTO> getCityById(@PathVariable Integer id) {
        CityDTO cityDTO = cityDTOMapper.apply(cityService.findById(id));

        return ResponseEntity.ok(cityDTO);
    }
}
