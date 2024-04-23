package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.GameUserSkillEmbedded;
import com.around.aroundcore.database.models.GameUserSkill;
import com.around.aroundcore.database.models.Skills;
import com.around.aroundcore.database.repositories.GameUserRepository;
import com.around.aroundcore.web.exceptions.entity.GameUserEmailNotUnique;
import com.around.aroundcore.web.exceptions.entity.GameUserNullException;
import com.around.aroundcore.web.exceptions.entity.GameUserUsernameNotUnique;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class GameUserService {

    private final GameUserRepository userRepository;
    private final SkillService skillService;

    public void create(GameUser user) {
        List<GameUserSkill> gameUserSkillList = new ArrayList<>();
        for (Skills skill : Skills.values()){
            GameUserSkill gameUserSkill = new GameUserSkill();
            gameUserSkill.setCurrentLevel(0);
            GameUserSkillEmbedded gameUserSkillEmbedded = new GameUserSkillEmbedded();
            gameUserSkillEmbedded.setGameUser(user);
            gameUserSkillEmbedded.setSkill(skillService.findById(skill.getId()));
            gameUserSkill.setGameUserSkillEmbedded(gameUserSkillEmbedded);
            gameUserSkillList.add(gameUserSkill);
        }
        user.setUserSkills(gameUserSkillList);
        userRepository.save(user);
    }

    public void update(GameUser user) {
        userRepository.save(user);
    }
    public GameUser findById(Integer id) throws GameUserNullException {
        return userRepository.findById(id).orElseThrow(GameUserNullException::new);
    }
    public GameUser findByEmail(String email) throws GameUserNullException {
        return userRepository.findByEmail(email).orElseThrow(GameUserNullException::new);
    }
    public List<GameUser> findAll() {
        return userRepository.findAll();
    }
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    public void checkEmail(String email){
        if (existByEmail(email)){
            throw new GameUserEmailNotUnique();
        }
    }
    public boolean existByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public void checkUsername(String username){
        if (existByUsername(username)){
            throw new GameUserUsernameNotUnique();
        }
    }
}
