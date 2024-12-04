package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.skill.Skill;
import com.around.aroundcore.database.services.GameUserSkillsService;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.database.services.SkillService;
import com.around.aroundcore.web.dtos.SkillDTO;
import com.around.aroundcore.web.mappers.SkillDTOMapper;
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
@RequestMapping(AroundConfig.API_V1_SKILLS)
@Tag(name="Skill Controller", description="Controller to get info about skills")
@SecurityRequirement(name = "JWT")
public class SkillController {
    private final SkillService skillService;
    private final GameUserSkillsService gameUserSkillsService;
    private final SessionService sessionService;
    private final SkillDTOMapper skillDTOMapper;
    @GetMapping("/all")
    @Operation(
            summary = "Gives info about skill by id",
            description = "Allows to get info about skill by id."
    )
    public ResponseEntity<List<SkillDTO>> handleGetAllSkills(){

        List<Skill> skills = skillService.findAll();
        List<SkillDTO> skillDTOS = skills.stream().map(skillDTOMapper).toList();

        return ResponseEntity.ok(skillDTOS);
    }
    @GetMapping("/{id}")
    @Operation(
            summary = "Gives info about skill by id",
            description = "Allows to get info about skill by id."
    )
    public ResponseEntity<SkillDTO> handleGetSkillById(@PathVariable @Parameter(description = "Skill id", example = "0") Integer id){
        var skill = skillService.findById(id);
        SkillDTO skillDTO = skillDTOMapper.apply(skill);

        return ResponseEntity.ok(skillDTO);
    }
    @PostMapping("/{id}/buyLevels")
    @Operation(
            summary = "Gives info about skill by id",
            description = "Allows to get info about skill by id."
    )
    @Transactional
    public ResponseEntity<String> handleGetSkillById(@PathVariable @Parameter(description = "Skill id", example = "0") Integer id,
                                                       @RequestParam("levels") @Schema(description = "number of levels you want buy") Integer levels){
        var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        var user = session.getUser();
        var skill = skillService.findById(id);

        gameUserSkillsService.buyLevelsOfGameUserSkill(user,skill,levels);

        return ResponseEntity.ok("");
    }
}
