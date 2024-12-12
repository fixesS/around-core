package com.around.aroundcore.database.services.user;

import com.around.aroundcore.database.models.user.GameUser;
import com.around.aroundcore.database.models.user.GameUserSkill;
import com.around.aroundcore.database.models.user.GameUserSkillEmbedded;
import com.around.aroundcore.database.models.skill.Skill;
import com.around.aroundcore.database.repositories.user.GameUserSkillsRepository;
import com.around.aroundcore.core.exceptions.api.LevelsLessOrEqualZeroException;
import com.around.aroundcore.core.exceptions.api.entity.GameUserSkillNullException;
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
        return gameUserSkillsRepository.findById(gameUserSkillEmbedded).orElseThrow(GameUserSkillNullException::new);
    }

    @Transactional
    public void buyLevelsOfGameUserSkill(GameUser user, Skill skill, Integer levels){
        if(levels<=0){
            throw new LevelsLessOrEqualZeroException();
        }
        GameUserSkill gameUserSkill;
        try{
            gameUserSkill = findByUserAndSkill(user, skill);
        }catch ( GameUserSkillNullException e) {
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
