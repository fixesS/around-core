package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.skill.Skill;
import com.around.aroundcore.database.repositories.SkillRepository;
import com.around.aroundcore.web.exceptions.api.entity.SkillNullException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class SkillService {
    private final SkillRepository skillRepository;
    public Skill findById(Integer id) throws SkillNullException {
        return skillRepository.findById(id).orElseThrow(SkillNullException::new);
    }
    public List<Skill> findAll(){
        return skillRepository.findAll();
    }
    public boolean existById(Integer id){
        return skillRepository.existsById(id);
    }
}
