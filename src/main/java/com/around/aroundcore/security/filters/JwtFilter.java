package com.around.aroundcore.security.filters;

import com.around.aroundcore.database.models.Session;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.security.services.JwtService;
import com.around.aroundcore.security.tokens.JwtAuthenticationToken;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.exceptions.auth.AuthHeaderNotStartsWithPrefixException;
import com.around.aroundcore.web.exceptions.auth.AuthHeaderNullException;
import com.around.aroundcore.web.exceptions.auth.AuthSessionNullException;
import com.around.aroundcore.web.exceptions.entity.SessionNullException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private SessionService sessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader;
        try {
            authHeader = jwtService.resolveAuthHeader(request);
        } catch (AuthHeaderNullException | AuthHeaderNotStartsWithPrefixException e) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken;
        try {
            accessToken = jwtService.resolveToken(authHeader);
        } catch (AuthHeaderNotStartsWithPrefixException e) {
            filterChain.doFilter(request, response);
            return;
        }

        try{
            jwtService.validateAccessToken(accessToken);
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired");
            filterChain.doFilter(request, response);
            return;
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt");
            filterChain.doFilter(request, response);
            return;
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt");
            filterChain.doFilter(request, response);
            return;
        } catch (RuntimeException e) {
            log.error("invalid token");
            filterChain.doFilter(request, response);
            return;
        }
        Session session;
        try{
            session = sessionService.findByUuid(jwtService.getSessionIdAccess(accessToken));
        }catch (SessionNullException e){
            log.error(e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        Claims claims = jwtService.getAccessClaims(accessToken);
        JwtAuthenticationToken authentication = new JwtAuthenticationToken(session);
        Date iat = claims.getIssuedAt();
        if(!iat.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().isAfter(session.getLastRefresh())){
            authentication.setAuthenticated(true);
        }else{
            log.error("Session expired");
            filterChain.doFilter(request, response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("JWT Auth complete");
        filterChain.doFilter(request, response);
    }

}
