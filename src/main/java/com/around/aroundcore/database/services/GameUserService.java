package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.repositories.GameUserRepository;
import com.around.aroundcore.web.exceptions.entity.GameUserEmailNotUnique;
import com.around.aroundcore.web.exceptions.entity.GameUserNullException;
import com.around.aroundcore.web.exceptions.entity.GameUserUsernameNotUnique;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class GameUserService {

    private final GameUserRepository userRepository;

    @Transactional
    public void create(GameUser user) {
        userRepository.save(user);
    }

    @Transactional
    public void update(GameUser user) {
        userRepository.save(user);
    }
    @Transactional
    public GameUser findById(Integer id) throws GameUserNullException {
        return userRepository.findById(id).orElseThrow(GameUserNullException::new);
    }
    @Transactional
    public GameUser findByEmail(String email) throws GameUserNullException {
        return userRepository.findByEmail(email).orElseThrow(GameUserNullException::new);
    }
    @Transactional
    public List<GameUser> findAll() {
        return userRepository.findAll();
    }
    @Transactional
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    public void checkEmail(String email){
        if (existByEmail(email)){
            throw new GameUserEmailNotUnique();
        }
    }
    @Transactional
    public boolean existByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public void checkUsername(String username){
        if (existByUsername(username)){
            throw new GameUserUsernameNotUnique();
        }
    }
}
