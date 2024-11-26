package com.around.aroundcore.security.filters;

import com.around.aroundcore.database.models.user.GameUser;
import com.around.aroundcore.database.models.Session;
import com.around.aroundcore.security.services.LoginPasscodeService;
import com.around.aroundcore.security.tokens.JwtAuthenticationToken;
import com.around.aroundcore.core.exceptions.auth.AuthHeaderNullException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.InetAddress;

@Slf4j
@AllArgsConstructor
public class LoginPasscodeFilter extends OncePerRequestFilter {
    private final LoginPasscodeService authService;
    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userAgent = request.getHeader("User-Agent");
        String ip = request.getRemoteAddr();
        String login;
        String passcode;
        try {
            login = authService.getLogin(request);
            passcode = authService.getPasscode(request);
        }catch (AuthHeaderNullException e) {
            log.debug("WEBSOCKET Auth header is null");
            filterChain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                login, passcode);
        Authentication auth = authenticationManager.authenticate(authenticationToken);
        GameUser user = (GameUser) auth.getPrincipal();
        Session session = authService.createSession(user, userAgent, InetAddress.getByName(ip));
        JwtAuthenticationToken authentication = new JwtAuthenticationToken(session);
        authentication.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("WEBSOCKET Auth complete");
        filterChain.doFilter(request, response);
    }
}
