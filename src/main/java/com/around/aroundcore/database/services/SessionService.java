package com.around.aroundcore.database.services;

import com.around.aroundcore.database.EntityFilters;
import com.around.aroundcore.database.models.user.GameUser;
import com.around.aroundcore.database.models.Session;
import com.around.aroundcore.database.repositories.SessionRepository;
import com.around.aroundcore.core.exceptions.api.entity.SessionNullException;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class SessionService {
    private final SessionRepository sessionRepository;
    private final EntityManager entityManager;

    @Transactional
    public void create(Session session){
        sessionRepository.save(session);
    }
    @Transactional
    public void update(Session session){
        sessionRepository.save(session);
    }
    @Transactional
    public Session findByUuid(UUID uuid) throws SessionNullException {
        org.hibernate.Session hibernateSession = entityManager.unwrap(org.hibernate.Session.class);
        hibernateSession.enableFilter(EntityFilters.ACTIVE_ROUND.getName()).setParameter(EntityFilters.ACTIVE_ROUND.getParameter(),true);
        Session session = sessionRepository.findOneBySessionUuid(uuid).orElseThrow(SessionNullException::new);
        hibernateSession.disableFilter(EntityFilters.ACTIVE_ROUND.getName());
        return session;
    }
    public List<Session> findAll(){
        return sessionRepository.findAll();
    }
    public void delete(Session session){
        sessionRepository.delete(session);
    }
    @Transactional
    public void deleteAllByGameUser(GameUser gameUser){
        sessionRepository.deleteAllByUser(gameUser);
    }
    @Transactional
    public void removeExpired(){
        List<Session> sessions = findAll();
        sessions.forEach(session -> {
            if(LocalDateTime.now().isAfter(session.getExpiresIn())){
                delete(session);
            }
        });
    }
}
