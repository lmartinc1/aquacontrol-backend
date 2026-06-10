package com.aquacontrol.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers("/api/hogares/**").hasAnyRole("COMITE", "TECNICO", "REPRESENTANTE")
                        .requestMatchers(HttpMethod.POST, "/api/hogares").hasRole("COMITE")
                        .requestMatchers(HttpMethod.PUT, "/api/hogares/**").hasRole("COMITE")
                        .requestMatchers(HttpMethod.DELETE, "/api/hogares/**").hasRole("COMITE")

                        .requestMatchers(HttpMethod.POST, "/api/aportes").hasRole("COMITE")
                        .requestMatchers(HttpMethod.GET, "/api/aportes/**").hasAnyRole("COMITE", "REPRESENTANTE")

                        .requestMatchers(HttpMethod.POST, "/api/problemas").hasAnyRole("COMITE", "REPRESENTANTE")
                        .requestMatchers(HttpMethod.GET, "/api/problemas/**").hasAnyRole("COMITE", "TECNICO")
                        .requestMatchers(HttpMethod.PUT, "/api/problemas/**").hasAnyRole("COMITE", "TECNICO")

                        .requestMatchers("/api/mantenimiento/**").hasAnyRole("COMITE", "TECNICO")

                        .requestMatchers(HttpMethod.POST, "/api/distribucion").hasRole("COMITE")
                        .requestMatchers(HttpMethod.GET, "/api/distribucion/**").hasAnyRole("COMITE", "REPRESENTANTE")

                        .requestMatchers("/api/tanque/**").hasAnyRole("COMITE", "TECNICO")

                        .requestMatchers("/api/avisos/**").hasAnyRole("COMITE", "REPRESENTANTE")
                        .requestMatchers(HttpMethod.POST, "/api/avisos").hasRole("COMITE")

                        .requestMatchers("/api/usuarios/**").hasRole("COMITE")
                        .requestMatchers("/api/reportes/**").hasRole("COMITE")

                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:5173",
                "http://localhost",
                "https://aquacontrol-frontend.onrender.com"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}