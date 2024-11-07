package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.database.repositories.TeamRepository;
import com.around.aroundcore.web.exceptions.api.entity.TeamNullException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class TeamService {
    private TeamRepository teamRepository;

    public Team findById(Integer id) throws TeamNullException {
        return teamRepository.findById(id).orElseThrow(TeamNullException::new);
    }
    public List<Team> findAll(){
        return teamRepository.findAll();
    }
    private boolean existById(Integer id){
        return teamRepository.existsById(id);
    }
    public void checkById(Integer id){
        if(!existById(id)){
            throw new TeamNullException();
        }
    }

}
