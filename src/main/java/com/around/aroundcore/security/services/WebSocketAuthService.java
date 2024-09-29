package com.around.aroundcore.security.services;

import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Session;
import com.around.aroundcore.database.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.net.InetAddress;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;


@RequiredArgsConstructor
public class WebSocketAuthService {
    private final SessionService sessionService;

    @Value("${jwt.token.access.expired}")
    private long accessExp;

    public Session createSession(GameUser user, String userAgent, InetAddress address){
        Session session = new Session();
        UUID sessionUuid = UUID.randomUUID();

        session.setUserAgent(userAgent);
        session.setSessionUuid(sessionUuid);
        session.setIp(address);
        session.setUser(user);

        Date now = new Date();
        Date expiration = new Date(now.getTime()+accessExp);

        session.setCreated(now.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());
        session.setExpiresIn(expiration.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());
        session.setLastRefresh(now.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());

        sessionService.create(session);
        return session;
    }
}
