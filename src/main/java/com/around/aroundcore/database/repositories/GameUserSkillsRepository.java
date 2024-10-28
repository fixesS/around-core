package com.around.aroundcore.database.repositories;

import com.around.aroundcore.database.models.GameUserSkill;
import com.around.aroundcore.database.models.GameUserSkillEmbedded;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameUserSkillsRepository extends JpaRepository<GameUserSkill, GameUserSkillEmbedded> {
}
