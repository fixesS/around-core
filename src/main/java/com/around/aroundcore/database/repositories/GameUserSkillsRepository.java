package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.GameUserSkillEmbedded;
import com.around.aroundcore.database.models.GameUserSkills;
import com.around.aroundcore.database.models.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameUserSkillsRepository extends JpaRepository<GameUserSkills, GameUserSkillEmbedded> {
}
