package com.around.aroundcore.database.repositories.user;

import com.around.aroundcore.database.models.user.GameUserSkill;
import com.around.aroundcore.database.models.user.GameUserSkillEmbedded;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameUserSkillsRepository extends JpaRepository<GameUserSkill, GameUserSkillEmbedded> {
}
