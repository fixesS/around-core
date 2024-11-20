package com.around.aroundcore.security.tokens;

import com.around.aroundcore.database.models.user.GameUser;
import com.around.aroundcore.database.models.Session;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken implements Authentication {

    private boolean authenticated;
    private final Session session;
    private final GameUser user;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return user.getEmail();
    }

    @Override
    public Object getDetails() {
        return user.getId();
    }

    @Override
    public Object getPrincipal() {
        return session.getSessionUuid();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {// IT IS USER IN WEBSOCKETS (MESSAGING TEMPLATE -> SEND TO USER)
        return user.getId().toString();
    }

    public JwtAuthenticationToken(
            Session session) {
        this.session = session;
        this.user = session.getUser();
    }

}
