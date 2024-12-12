package com.around.aroundcore.security.services;

import com.around.aroundcore.database.models.user.GameUser;
import com.around.aroundcore.database.models.Session;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.security.models.Token;
import com.around.aroundcore.web.dtos.auth.TokenData;
import lombok.AllArgsConstructor;

import java.net.InetAddress;
import java.time.ZoneId;
import java.util.UUID;

@AllArgsConstructor
public class AuthService {
    private final String timeLocale;
    private final JwtService jwtService;
    private final SessionService sessionService;


    public TokenData createSession(GameUser user, String userAgent, InetAddress address){

        Session session = new Session();
        UUID sessionUuid = UUID.randomUUID();

        session.setUserAgent(userAgent);
        session.setSessionUuid(sessionUuid);
        session.setIp(address);
        session.setUser(user);

        Token accessToken = jwtService.generateAccessToken(session.getSessionUuid());
        Token refreshToken = jwtService.generateRefreshToken(session.getSessionUuid());

        session.setCreated(accessToken.getCreated().toInstant()
                .atZone(ZoneId.of(timeLocale))
                .toLocalDateTime());
        session.setExpiresIn(refreshToken.getExpiresIn().toInstant()
                .atZone(ZoneId.of(timeLocale))
                .toLocalDateTime());
        session.setLastRefresh(accessToken.getCreated().toInstant()
                .atZone(ZoneId.of(timeLocale))
                .toLocalDateTime());

        sessionService.create(session);

        return TokenData.builder()
                .access_token(accessToken.getToken())
                .refresh_token(refreshToken.getToken())
                .build();
    }

    public TokenData refreshSession(UUID sessionUuid){

        Session session = sessionService.findByUuid(sessionUuid);

        Token accessToken = jwtService.generateAccessToken(sessionUuid);
        Token refreshToken = jwtService.generateRefreshToken(session.getSessionUuid());

        session.setLastRefresh(accessToken.getCreated().toInstant()
                .atZone(ZoneId.of(timeLocale))
                .toLocalDateTime());
        session.setExpiresIn(accessToken.getExpiresIn().toInstant()
                .atZone(ZoneId.of(timeLocale))
                .toLocalDateTime());

        sessionService.update(session);

        return TokenData.builder()
                .access_token(accessToken.getToken())
                .refresh_token(refreshToken.getToken())
                .build();
    }
}
