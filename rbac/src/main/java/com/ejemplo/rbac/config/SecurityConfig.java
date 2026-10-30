package com.ejemplo.rbac.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Permite a "Usuario" y "Administrador" crear usuarios (POST /api/usuarios)
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").hasAnyRole("USER", "ADMIN")

                        // Restringe todas las demás operaciones de /api/usuarios al rol "Administrador"
                        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")

                        // Restringe toda la gestión de /api/productos al rol "Administrador"
                        .requestMatchers("/api/productos/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails usuario = User.builder()
                .username("user")
                .password("{noop}1234")
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}1234")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(usuario, admin);
    }
}
.authorizeHttpRequests(auth -> auth
        .requestMatchers("/", "/index.html").permitAll()   // <- nueva
    .requestMatchers(HttpMethod.POST, "/api/usuarios").hasAnyRole("USUARIO", "ADMIN")
    .requestMatchers("/api/usuarios/**").hasRole("ADMIN")
    .requestMatchers("/api/productos/**").hasRole("ADMIN")
    .anyRequest().denyAll()
)