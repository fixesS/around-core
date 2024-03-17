package com.around.aroundcore.config;

import com.around.aroundcore.database.models.Role;
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
            "/api/v1/auth/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(AUTH_WHITE_LIST).permitAll()
                        .requestMatchers("/api/**").hasAuthority(Role.USER.name())
                        .requestMatchers("/ws/**").hasAnyAuthority(Role.USER.name(),Role.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .sessionManagement(smc -> smc.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(jwtConfig.authenticationManager())
                .addFilterBefore(jwtConfig.jwtAuthenticationFilter(), BasicAuthenticationFilter.class)
        ;
        return http.build();
    }


}
