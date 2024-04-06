package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.repositories.TeamRepository;
import com.around.aroundcore.web.exceptions.entity.GameChunkNullException;
import com.around.aroundcore.web.exceptions.entity.TeamNullException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TeamService {
    private TeamRepository teamRepository;

    @Transactional
    public Team findById(Integer id) throws TeamNullException {
        return teamRepository.findById(id).orElseThrow(TeamNullException::new);
    }
    @Transactional
    public List<Team> findAll(){
        return teamRepository.findAll();
    }
    @Transactional
    public boolean existById(Integer id){
        return teamRepository.existsById(id);
    }

}
