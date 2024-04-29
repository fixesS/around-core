package com.around.aroundcore.config;

import com.around.aroundcore.database.models.Role;
import com.around.aroundcore.security.filters.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    HandlerExceptionResolver resolver;

    public static final String[] WHITE_LIST = {
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v2/api-docs/**",
            "/swagger-resources/**",
            AroundConfig.API_V1_AUTH+"/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/**").hasAuthority(Role.USER.name())
                        .requestMatchers("/ws/**").hasAuthority(Role.USER.name())
                        .anyRequest().authenticated()
                )
                .sessionManagement(smc -> smc.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(jwtConfig.authenticationManager())
                .addFilterBefore(new JwtFilter(jwtConfig.jwtService(), jwtConfig.sessionService), BasicAuthenticationFilter.class)
                .addFilterBefore(jwtConfig.exceptionHandlerFilter(), JwtFilter.class)
        ;
        return http.build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer()  {
        return (web) -> web.ignoring().requestMatchers(WHITE_LIST);
    }



}
