package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.repositories.GameUserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GameUserService {
    private GameUserRepository userRepository;

    @Transactional
    public void create(GameUser user){
        userRepository.save(user);
    }

    @Transactional
    public void update(GameUser user){
        userRepository.save(user);
    }
    @Transactional
    public GameUser findById(Integer id){
        return userRepository.findById(id).orElse(null);
    }
    @Transactional
    public GameUser findByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }
    @Transactional
    public List<GameUser> findAll(){
        return userRepository.findAll();
    }
}
