package com.peters.Keycloak.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig_old {
    private final JwtAuthConverter jwtAuthConverter;
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers(HttpMethod.GET, "/api/v1/restaurants/public/list").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/restaurants/public/menu/*").permitAll()
                            .requestMatchers( "/swagger-ui/**",
                                    "/v2/api-docs",
                                    "/v3/api-docs/**",
                                    "/swagger-resources/**",
                                    "/configuration/ui",
                                    "/configuration/security").permitAll()
                            .anyRequest().authenticated();
                });

        // OAuth2 Resource Server Configuration
        http.oauth2ResourceServer(resourceServer ->
                        resourceServer.jwt(jwtConfigurer ->
                                jwtConfigurer.jwtAuthenticationConverter(jwtAuthConverter))
//                        .opaqueToken(opaqueTokenConfigurer -> {})
        );

        // Session Management Configuration
        http.sessionManagement(sessionManagementConfigurer ->
                sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        return http.build();
    }

    @Bean
    public DefaultMethodSecurityExpressionHandler mSecurityHandler(){
        DefaultMethodSecurityExpressionHandler expressionHandler =
                new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setDefaultRolePrefix("");
        return expressionHandler;
    }
}
