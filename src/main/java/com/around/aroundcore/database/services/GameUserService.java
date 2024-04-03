package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.repositories.GameUserRepository;
import com.around.aroundcore.web.exceptions.entity.GameUserNullException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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

    public GameUser findById(Integer id) throws GameUserNullException {
        return userRepository.findById(id).orElseThrow(GameUserNullException::new);
    }

    public GameUser findByEmail(String email) throws GameUserNullException {
        return userRepository.findByEmail(email).orElseThrow(GameUserNullException::new);
    }

    public List<GameUser> findAll() {
        return userRepository.findAll();
    }


    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
