package org.mickleak.testcontainerspostgreflyway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsService simpleUserDetailsService) throws Exception {
        http
                .cors(Customizer.withDefaults()) // Default sorgt dafür, dass die CorsConfigurationSource Bean verwendet wird
                .csrf(AbstractHttpConfigurer::disable) // CSRF für REST API deaktivieren
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/").permitAll() // Root ohne Auth
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll() // Health und Info ohne Auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll() // Swagger UI ohne Auth
                        .anyRequest().authenticated() // Alle anderen Endpunkte benötigen Authentifizierung
                )
                .httpBasic(Customizer.withDefaults()) // Basic Authentication aktivieren
                .userDetailsService(simpleUserDetailsService);

        return http.build();
    }

    @Bean
    public UserDetailsService simpleUserDetailsService() {
        return username -> {
            List<GrantedAuthority> authorities = "admin".equals(username) ? List.of((GrantedAuthority) () -> "entity_edit") : List.of();
            return new User(username, "", authorities);
        };
    }
}
