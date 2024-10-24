package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.*;
import com.around.aroundcore.web.enums.Skills;
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
public class GameUserService {

    private final GameUserRepository userRepository;
    private final SkillService skillService;

    @Transactional
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
        user.addSkillToUserSkillList(gameUserSkillList);
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
    public GameUser findByUsername(String username) throws GameUserNullException {
        return userRepository.findByUsername(username).orElseThrow(GameUserNullException::new);
    }
    public List<GameUser> findByUsernameContaining(String username) {
        return userRepository.findByUsernameContaining(username);
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
    public List<GameUser> getTop5(){
        return userRepository.getStatTop5();
    }
    public List<GameUser> getTopAll(){
        return userRepository.getStatTopAll();
    }
    public void updateTeamForRound(GameUser user, Team team, Round round){
        userRepository.setTeamForRound(round.getId(), team.getId(), user.getId());
    }
    public void updateCityForRound(GameUser user, City city, Round round){
        userRepository.setCityForRound(round.getId(), city.getId(), user.getId());
    }
    public void createTeamCityForRound(GameUser user, Team team, City city, Round round){
        userRepository.createTeamAndCityForRound(round.getId(), team.getId(), city.getId(), user.getId());
    }
}
