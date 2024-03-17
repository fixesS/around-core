package com.around.aroundcore.security.jwt;

import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Session;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken implements Authentication {

    private boolean authenticated;
    @Setter
    private Session session;
    @Setter
    private GameUser user;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return user.getId();
    }

    @Override
    public Object getPrincipal() {
        return user.getUsername();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = authenticated;
    }

    @Override
    public String getName() {
        return null;
    }

    public JwtAuthenticationToken(
            Session session,
            GameUser user ) {
        this.session = session;
        this.user = user;

    }

}
