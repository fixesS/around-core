package com.around.aroundcore.security.filters;

import com.around.aroundcore.web.exceptions.auth.AuthHeaderException;
import com.around.aroundcore.web.exceptions.entity.SessionNullException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private HandlerExceptionResolver resolver;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);

        } catch (AuthHeaderException e) {
            log.error("Spring Security Filter chain auth header exception: {}", e.getClass());
            resolver.resolveException(request, response, null, e);
        } catch (SessionNullException e) {
            log.error("Spring Security Filter chain session null exception: {}", e.getClass());
            resolver.resolveException(request, response, null, e);
        } catch (JwtException e) {
            log.error("Spring Security Filter chain jwt exception: {}", e.getClass());
            resolver.resolveException(request, response, null, e);
        } catch (RuntimeException e) {
            log.error("Spring Security Filter runtime exception: {}",e.getMessage());
        }
    }
}
