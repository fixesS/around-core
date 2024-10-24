package com.around.aroundcore.config;

import com.around.aroundcore.database.repositories.GameUserRepository;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.security.filters.ExceptionHandlerFilter;
import com.around.aroundcore.security.filters.JwtFilter;
import com.around.aroundcore.security.filters.LoginPasscodeFilter;
import com.around.aroundcore.security.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${around.time.locale}")
    public String timeLocale;
    @Autowired
    public AuthConfig(GameUserRepository userRepository, SessionService sessionService, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver){
        this.userRepository = userRepository;
        this.sessionService = sessionService;
        this.resolver = resolver;
    }
    private final GameUserRepository userRepository;
    public final SessionService sessionService;
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
    public JwtFilter jwtFilter(){
        return new JwtFilter(timeLocale,jwtService(),sessionService);
    }
    @Bean
    public LoginPasscodeFilter webSocketFilter(){
        return new LoginPasscodeFilter(loginPasscodeAuthService(),authenticationManager());
    }
    @Bean
    public ExceptionHandlerFilter exceptionHandlerFilter(){
        return new ExceptionHandlerFilter(resolver);
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
        return new AuthService(timeLocale,jwtService(),sessionService);
    }
    @Bean
    public LoginPasscodeService loginPasscodeAuthService(){
        return new LoginPasscodeService(sessionService);
    }
}

