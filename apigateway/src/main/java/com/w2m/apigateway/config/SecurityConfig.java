package com.w2m.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityWebFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated())
                        .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                    );  
        return http.build();
    }
}