package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Session;
import com.around.aroundcore.database.repositories.SessionRepository;
import com.around.aroundcore.web.exceptions.api.entity.SessionNullException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class SessionService {
    private final SessionRepository sessionRepository;

    public void create(Session session){
        sessionRepository.save(session);
    }
    public void update(Session session){
        sessionRepository.save(session);
    }
    public Session findByUuid(UUID uuid) throws SessionNullException {
        return sessionRepository.findBySessionUuid(uuid).orElseThrow(SessionNullException::new);
    }
    public List<Session> findAll(){
        return sessionRepository.findAll();
    }
    public void delete(Session session){
        sessionRepository.delete(session);
    }
    public void deleteAllByGameUser(GameUser gameUser){
        sessionRepository.deleteAllByUser(gameUser);
    }
    public void removeExpired(){
        List<Session> sessions = findAll();
        sessions.forEach(session -> {
            if(LocalDateTime.now().isAfter(session.getExpiresIn())){
                delete(session);
            }
        });
    }
}
