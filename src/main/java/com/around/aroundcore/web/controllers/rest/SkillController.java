package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.Skill;
import com.around.aroundcore.database.services.SkillService;
import com.around.aroundcore.web.dtos.SkillDTO;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.exceptions.entity.SkillNullException;
import com.around.aroundcore.web.mappers.SkillDTOMapper;
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

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AroundConfig.API_V1_SKILLS)
@Tag(name="Skill Controller", description="Controller to get info about skills")
@SecurityRequirement(name = "JWT")
public class SkillController {

    private final SkillService skillService;
    private final SkillDTOMapper skillDTOMapper;
    @GetMapping("{id}")
    @Operation(
            summary = "Gives info about skill by id",
            description = "Allows to get info about skill by id."
    )
    public ResponseEntity<SkillDTO> handleGetSkillById(@PathVariable @Parameter(description = "Skill id", example = "0") Integer id){
        ApiResponse response;
        Skill skill;
        SkillDTO skillDTO = null;

        try {
            skill = skillService.findById(id);
            skillDTO = skillDTOMapper.apply(skill);
            response = ApiResponse.OK;
        }catch (SkillNullException e) {
            response = ApiResponse.SKILL_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                return new ResponseEntity<>(skillDTO,response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
}
