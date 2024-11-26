package com.around.aroundcore.security.filters;

import com.around.aroundcore.core.exceptions.auth.AuthHeaderException;
import com.around.aroundcore.core.exceptions.api.entity.SessionNullException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
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

        } catch (HttpRequestMethodNotSupportedException e){
            log.error("Spring Security Filter chain [METHOD] exception: {}", e.getClass());
            resolver.resolveException(request, response, null, e);
            filterChain.doFilter(request, response);
        }catch (AuthHeaderException e) {
            log.error("Spring Security Filter chain [AUTH HEADER] exception: {}", e.getClass());
            filterChain.doFilter(request, response);
            resolver.resolveException(request, response, null, e);
        } catch (SessionNullException e) {
            log.error("Spring Security Filter chain [SESSION NULL] exception: {}", e.getClass());
            filterChain.doFilter(request, response);
            resolver.resolveException(request, response, null, e);
        } catch (JwtException e) {
            log.error("Spring Security Filter chain [JWT] exception: {}", e.getClass());
            resolver.resolveException(request, response, null, e);
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            log.error("Spring Security Filter [RUNTIME] exception: {}",e.getMessage());
            resolver.resolveException(request, response, null, e);
            filterChain.doFilter(request, response);
        }
    }
}
