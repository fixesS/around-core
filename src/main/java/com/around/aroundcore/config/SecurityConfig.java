package com.around.aroundcore.config;

import com.around.aroundcore.database.models.Role;
import com.around.aroundcore.security.filters.JwtFilter;
import com.around.aroundcore.security.filters.LoginPasscodeFilter;
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
    public SecurityConfig(AuthConfig authConfig) {
        this.authConfig = authConfig;
    }
    private final AuthConfig authConfig;
    protected static final String[] WHITE_LIST = {
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v2/api-docs/**",
            "/swagger-resources/**",
            AroundConfig.API_V1_AUTH+"/**",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(WHITE_LIST).permitAll()
                        .requestMatchers("/api/**").hasAuthority(Role.USER.name())
                        .requestMatchers("/ws/**").hasAuthority(Role.USER.name())
                        .requestMatchers("/actuator/health").hasAuthority(Role.USER.name())
                        .anyRequest().authenticated()
                )
                .sessionManagement(smc -> smc.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(authConfig.authenticationManager())
                .addFilterBefore(authConfig.webSocketFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(authConfig.jwtFilter(), LoginPasscodeFilter.class)
                .addFilterBefore(authConfig.exceptionHandlerFilter(), JwtFilter.class);
        return http.build();
    }
}
