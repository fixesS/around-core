package com.around.aroundcore.database.services;

import com.around.aroundcore.database.EntityFilters;
import com.around.aroundcore.database.models.*;
import com.around.aroundcore.database.repositories.UserRoundTeamRepository;
import com.around.aroundcore.web.enums.Skills;
import com.around.aroundcore.database.repositories.GameUserRepository;
import com.around.aroundcore.web.exceptions.api.entity.GameUserEmailNotUnique;
import com.around.aroundcore.web.exceptions.api.entity.GameUserNullException;
import com.around.aroundcore.web.exceptions.api.entity.GameUserUsernameNotUnique;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class GameUserService {
    private final GameUserRepository userRepository;
    private final SkillService skillService;
    private final EntityManager entityManager;
    private final Random random = new Random();
    private final UserRoundTeamRepository userRoundTeamRepository;

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
    public void updateAndFlush(GameUser user) {
        userRepository.saveAndFlush(user);
    }
    @Transactional
    public GameUser findById(Integer id) throws GameUserNullException {
        org.hibernate.Session hibernateSession = entityManager.unwrap(org.hibernate.Session.class);
        hibernateSession.enableFilter(EntityFilters.ACTIVE_ROUND.getName()).setParameter(EntityFilters.ACTIVE_ROUND.getParameter(),true);
        GameUser user = userRepository.findOneById(id).orElseThrow(GameUserNullException::new);
        hibernateSession.disableFilter(EntityFilters.ACTIVE_ROUND.getName());
        return user;

    }
    @Transactional
    public GameUser findByEmail(String email) throws GameUserNullException {
        org.hibernate.Session hibernateSession = entityManager.unwrap(org.hibernate.Session.class);
        hibernateSession.enableFilter(EntityFilters.ACTIVE_ROUND.getName()).setParameter(EntityFilters.ACTIVE_ROUND.getParameter(),true);
        GameUser user =  userRepository.findByEmail(email).orElseThrow(GameUserNullException::new);
        hibernateSession.disableFilter(EntityFilters.ACTIVE_ROUND.getName());
        return user;
    }
    @Transactional
    public GameUser findByUsername(String username) throws GameUserNullException {
        org.hibernate.Session hibernateSession = entityManager.unwrap(org.hibernate.Session.class);
        hibernateSession.enableFilter(EntityFilters.ACTIVE_ROUND.getName()).setParameter(EntityFilters.ACTIVE_ROUND.getParameter(),true);
        GameUser user =  userRepository.findByUsername(username).orElseThrow(GameUserNullException::new);
        hibernateSession.disableFilter(EntityFilters.ACTIVE_ROUND.getName());
        return user;
    }
    public void increaseCapturedChunksToUserInRoundInCity(GameUser user, Round round, City city, Integer amount){
        if(amount>=0){
            userRoundTeamRepository.increaseCapturedChunks(user.getId(), round.getId(),city.getId(),amount);
        }
    }
    public List<GameUser> findByUsernameContaining(String username) {
        return  userRepository.findByUsernameContaining(username);
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
    public List<GameUser> getTopUsersChunksAll(List<Round> rounds, List<GameUser> users, Integer limit){
        List<Integer> roundIds = rounds.stream().map(Round::getId).collect(
                Collectors.collectingAndThen(Collectors.toList(),list ->list.isEmpty()? null : list));
        List<Integer> userIds = users.stream().map(GameUser::getId).collect(
                Collectors.collectingAndThen(Collectors.toList(),list ->list.isEmpty()? null : list));
        if(limit>100){
            limit = 100;
        }
        if(limit<1){
            limit = 1;
        }
        return userRepository.getUsersStatTopForRoundsForChunksAll(roundIds,userIds,limit);
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
    public GameUser findByOAuthIdAndProvider(String oauthId, OAuthProvider provider){
        return userRepository.findByOAuthIdAndProvider(oauthId,provider.name()).orElseThrow(GameUserNullException::new);
    }
    public boolean isOAuthProviderAccountAdded(GameUser user,OAuthProvider provider){
        return  userRepository.existsByUserIdAndProvider(user.getId(), provider.name());
    }
    public String generateUsername(String name){
        log.info("Creating username");
        String username = name;
        for(int i = 1; i<=50 && existByUsername(username); i++){
            log.info("loop "+username);
            username = name +"-"+ i;
        }
        if(existByUsername(username)){
            throw new GameUserUsernameNotUnique();
        }
        return username;
    }
    public Integer getNextId(){
        return userRepository.getNextValMySequence().intValue();
    }
    public String generatePassword(){
        log.info("Creating password");
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String specialChars = "!@#%^&*()_+-=:;\"'{}<>?|`~,.";

        StringBuilder password = new StringBuilder();
        password.append(uppercase.charAt(random.nextInt(0,uppercase.length())));
        password.append(lowercase.charAt(random.nextInt(0,uppercase.length())));
        for (int i = 0; i < 8; i++) {
            if(random.nextBoolean()){
                password.append(uppercase.charAt(random.nextInt(uppercase.length())));
            }else{
                password.append(lowercase.charAt(random.nextInt(lowercase.length())));
            }
        }
        password.append(random.nextInt(0,10000));
        password.append(specialChars.charAt(random.nextInt(0,specialChars.length())));
        return password.toString();
    }
}
