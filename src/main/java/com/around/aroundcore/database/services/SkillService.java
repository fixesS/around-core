package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.Skill;
import com.around.aroundcore.database.repositories.SkillRepository;
import com.around.aroundcore.web.exceptions.entity.SkillNullException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SkillService {
    private final SkillRepository skillRepository;
    @Transactional
    public Skill findById(Integer id) throws SkillNullException {
        return skillRepository.findById(id).orElseThrow(SkillNullException::new);
    }
    @Transactional
    public List<Skill> findAll(){
        return skillRepository.findAll();
    }
    @Transactional
    public boolean existById(Integer id){
        return skillRepository.existsById(id);
    }
}
