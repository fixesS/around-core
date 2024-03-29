package com.around.aroundcore.security.filters;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.Session;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.security.jwt.JwtAuthenticationToken;
import com.around.aroundcore.security.jwt.JwtService;
import com.around.aroundcore.web.exceptions.AuthHeaderNotStartsWithPrefixException;
import com.around.aroundcore.web.exceptions.AuthHeaderNullException;
import com.around.aroundcore.web.exceptions.SessionNullException;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final RequestMatcher ignoredPaths = new AntPathRequestMatcher(AroundConfig.API_V1_AUTH);
    private JwtService jwtService;
    private SessionService sessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (this.ignoredPaths.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        boolean auth = false;
        final String authHeader;
        try {
            authHeader = jwtService.resolveAuthHeader(request);
        } catch (AuthHeaderNullException e) {
            String errmsg = "Auth header is null";
            log.debug(errmsg, e);
            e.setMessage(errmsg);
            throw e;
        } catch (AuthHeaderNotStartsWithPrefixException e) {
            String errmsg = String.format("Auth header does not starts with '%s'",JwtService.JWT_PREFIX);
            log.debug(errmsg, e);
            e.setMessage(errmsg);
            throw e;
        }

        String accessToken = null;
        try {
            accessToken = jwtService.resolveToken(authHeader);
        } catch (AuthHeaderNotStartsWithPrefixException e) {
            String errmsg = String.format("Auth header does not starts with '%s'",JwtService.JWT_PREFIX);
            log.debug(errmsg, e);
            e.setMessage(errmsg);
            throw e;
        }

        try{
            jwtService.validateAccessToken(accessToken);
        } catch (ExpiredJwtException expEx) {
            log.debug("Token expired", expEx);
            throw expEx;
        } catch (UnsupportedJwtException unsEx) {
            log.debug("Unsupported jwt", unsEx);
            throw unsEx;
        } catch (MalformedJwtException mjEx) {
            log.debug("Malformed jwt", mjEx);
            throw mjEx;
        } catch (RuntimeException e) {
            log.debug("invalid token", e);
            throw e;
        }
        Session session = null;
        try{
            session = sessionService.findByUuid(jwtService.getSessionIdAccess(accessToken));
        }catch (SessionNullException e){
            String errmsg = "Session is null";
            log.debug(errmsg, e);
            e.setMessage(errmsg);
            throw e;
        }

        Claims claims = jwtService.getAccessClaims(accessToken);
        JwtAuthenticationToken authentication = new JwtAuthenticationToken(session,session.getUser());
        Date iat = claims.getIssuedAt();
        if(!iat.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().isAfter(session.getLastRefresh())){
            authentication.setAuthenticated(true);
        }else{
            log.debug("Session expired");
            authentication.setAuthenticated(false);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
