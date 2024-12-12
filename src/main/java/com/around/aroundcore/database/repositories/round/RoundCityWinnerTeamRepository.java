package com.around.aroundcore.database.repositories.round;

import com.around.aroundcore.database.models.round.RoundCityWinnerTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoundCityWinnerTeamRepository extends JpaRepository<RoundCityWinnerTeam, Integer> {
}
