package com.around.aroundcore.config;

import com.around.aroundcore.database.repositories.GameUserRepository;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.security.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class AuthConfig {
    @Autowired
    private GameUserRepository userRepository;
    @Autowired
    public SessionService sessionService;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    HandlerExceptionResolver resolver;

    @Bean
    public UserDetailsService userDetailsService(){
        return new GameUserDetailsServiceImpl(
                userRepository
        );
    }
    @Bean
    public JwtService jwtService(){
        return new JwtService();
    }
    @Bean
    public WebSocketAuthService webSocketAuthService(){
        return new WebSocketAuthService(sessionService);
    }
    @Bean
    public WebSocketHeaderService webSocketHeaderService(){
        return  new WebSocketHeaderService();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    public AuthService authService(){
        return new AuthService(jwtService(),sessionService);
    }
}

