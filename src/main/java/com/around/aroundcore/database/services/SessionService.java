package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.Session;
import com.around.aroundcore.database.repositories.SessionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class SessionService {

    private SessionRepository sessionRepository;

    @Transactional
    public void create(Session session){
        sessionRepository.save(session);
    }
    @Transactional
    public void update(Session session){
        sessionRepository.save(session);
    }
    @Transactional
    public Session findByUuid(UUID uuid){
        return sessionRepository.findBySessionUuid(uuid).orElse(null);
    }
    @Transactional
    public List<Session> findAll(){
        return sessionRepository.findAll();
    }
    @Transactional
    public void delete(Session session){
        sessionRepository.delete(session);
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