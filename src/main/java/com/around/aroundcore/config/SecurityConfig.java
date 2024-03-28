package com.around.aroundcore.config;

import com.around.aroundcore.database.models.Role;
import com.around.aroundcore.security.filters.ExceptionHandlerFilter;
import com.around.aroundcore.security.filters.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtConfig jwtConfig;
    private static final String[] AUTH_WHITE_LIST = {
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v2/api-docs/**",
            "/swagger-resources/**",
            "/api/v1/auth/**",
            "/ws/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(AUTH_WHITE_LIST).permitAll()
                        .requestMatchers("/api/**").hasAuthority(Role.USER.name())
                        //.requestMatchers("/ws/**").permitAll()
                        .anyRequest().permitAll()
                )
                .sessionManagement(smc -> smc.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(jwtConfig.authenticationManager())
                .addFilterBefore(jwtConfig.jwtAuthenticationFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(jwtConfig.exceptionHandlerFilter(), JwtFilter.class)
        ;
        return http.build();
    }


}
