package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.GameUserSkill;
import com.around.aroundcore.database.models.GameUserSkillEmbedded;
import com.around.aroundcore.database.models.Skill;
import com.around.aroundcore.database.repositories.GameUserSkillsRepository;
import com.around.aroundcore.web.exceptions.api.LevelsLessOrEqualZero;
import com.around.aroundcore.web.exceptions.entity.GameUserSkillsNullException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Transactional
@Service
public class GameUserSkillsService {
    private final GameUserSkillsRepository gameUserSkillsRepository;

    public GameUserSkill findByUserAndSkill(GameUser user, Skill skill){
        GameUserSkillEmbedded gameUserSkillEmbedded = new GameUserSkillEmbedded();
        gameUserSkillEmbedded.setGameUser(user);
        gameUserSkillEmbedded.setSkill(skill);
        return gameUserSkillsRepository.findById(gameUserSkillEmbedded).orElseThrow(GameUserSkillsNullException::new);
    }

    @Transactional
    public void buyLevelsOfGameUserSkill(GameUser user, Skill skill, Integer levels){
        if(levels<=0){
            throw new LevelsLessOrEqualZero();
        }
        GameUserSkill gameUserSkill;
        try{
            gameUserSkill = findByUserAndSkill(user, skill);
        }catch ( GameUserSkillsNullException e) {
            gameUserSkill = new GameUserSkill();
            gameUserSkill.setCurrentLevel(0);
            GameUserSkillEmbedded gameUserSkillEmbedded = new GameUserSkillEmbedded();
            gameUserSkillEmbedded.setGameUser(user);
            gameUserSkillEmbedded.setSkill(skill);
            gameUserSkill.setGameUserSkillEmbedded(gameUserSkillEmbedded);
            gameUserSkillsRepository.saveAndFlush(gameUserSkill);
        }

        Integer newLevel = gameUserSkill.getFutureLevel(levels);
        Integer newLevelCost = skill.getCost().getCostForNewLevelByOldLevel(gameUserSkill.getCurrentLevel(), newLevel);
        gameUserSkill.addLevel(levels);
        user.reduceCoins(newLevelCost);

        gameUserSkillsRepository.saveAndFlush(gameUserSkill);
    }
}
