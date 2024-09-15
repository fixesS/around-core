package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.Skill;
import com.around.aroundcore.database.services.GameUserSkillsService;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.database.services.SkillService;
import com.around.aroundcore.web.dtos.SkillDTO;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.exceptions.api.LevelsLessOrEqualZero;
import com.around.aroundcore.web.exceptions.entity.*;
import com.around.aroundcore.web.mappers.SkillDTOMapper;
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
@RequestMapping(AroundConfig.API_V1_SKILLS)
@Tag(name="Skill Controller", description="Controller to get info about skills")
@SecurityRequirement(name = "JWT")
public class SkillController {
    private final SkillService skillService;
    private final GameUserSkillsService gameUserSkillsService;
    private final SessionService sessionService;
    private final SkillDTOMapper skillDTOMapper;
    @GetMapping("all")
    @Operation(
            summary = "Gives info about skill by id",
            description = "Allows to get info about skill by id."
    )
    public ResponseEntity<List<SkillDTO>> handleGetAllSkills(){
        ApiResponse response;

        List<Skill> skills = skillService.findAll();
        List<SkillDTO> skillDTOS = skills.stream().map(skillDTOMapper).toList();

        response = ApiResponse.OK;
        return new ResponseEntity<>(skillDTOS,response.getStatus());
    }
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
    @PostMapping("{id}/buyLevels")
    @Operation(
            summary = "Gives info about skill by id",
            description = "Allows to get info about skill by id."
    )
    public ResponseEntity<String> handleGetSkillById(@PathVariable @Parameter(description = "Skill id", example = "0") Integer id,
                                                       @RequestParam("levels") @Schema(description = "number of levels you want buy") Integer levels){
        ApiResponse response;

        try {
            var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var session = sessionService.findByUuid(sessionUuid);
            var user = session.getUser();
            var skill = skillService.findById(id);

            gameUserSkillsService.buyLevelsOfGameUserSkill(user,skill,levels);
            response = ApiResponse.OK;
        }catch (GameUserNullException e) {
            response = ApiResponse.USER_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }catch (SkillNullException | GameUserSkillsNullException e) {
            response = ApiResponse.SKILL_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }catch (GameUserNotEnoughCoins e){
            response = ApiResponse.USER_NOT_ENOUGH_COINS;
            log.error(e.getMessage());
        }catch (GameUserSkillUnreachebleLevel e){
            response = ApiResponse.SKILL_LEVEL_UNREACHABLE;
            log.error(e.getMessage());
        }catch (GameUserSkillAlreadyMaxLevel e){
            response = ApiResponse.SKILL_LEVEL_ALREADY_MAX;
            log.error(e.getMessage());
        }catch (LevelsLessOrEqualZero e){
            response = ApiResponse.LEVELS_MUST_BE_MORE_THAN_ZERO;
            log.error(e.getMessage());
        }

        switch (response){
            case OK -> {
                return new ResponseEntity<>("",response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
}
