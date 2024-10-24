package com.around.aroundcore.security.services;

import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Session;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.web.exceptions.auth.AuthHeaderNullException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.ServerHttpRequest;

import java.net.InetAddress;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;


@RequiredArgsConstructor
public class LoginPasscodeService {
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
    public String getLogin(ServerHttpRequest request){
        return getAuthHeaderByName(request, "login");
    }
    public String getLogin(HttpServletRequest request){
        return getAuthHeaderByName(request, "login");
    }
    public String getPasscode(ServerHttpRequest request){
        return getAuthHeaderByName(request, "passcode");
    }
    public String getPasscode(HttpServletRequest request){
        return getAuthHeaderByName(request, "passcode");
    }
    private String getAuthHeaderByName(ServerHttpRequest request, String headerName){
        String value = request.getHeaders().getFirst(headerName);
        if(value == null) throw new AuthHeaderNullException();
        return value;
    }
    private String getAuthHeaderByName( HttpServletRequest request, String headerName){
        String value = request.getHeader(headerName);
        if(value == null) throw new AuthHeaderNullException();
        return value;
    }
    public String getHeader(ServerHttpRequest request, String headerName){
        return request.getHeaders().getFirst(headerName);
    }
}
