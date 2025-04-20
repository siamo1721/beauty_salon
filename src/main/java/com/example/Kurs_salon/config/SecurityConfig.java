package com.example.Kurs_salon.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/login.html",
                                "/register.html",
                                "/api/auth/**",
                                "/appointments.html",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        .requestMatchers("/admin/**", "/api/admin/**").hasAuthority("ADMIN")

                        .requestMatchers("/system-admin/**", "/api/system-admin/**").hasAnyAuthority("SYSTEM_ADMIN","ADMIN")

                        .requestMatchers("/master/**", "/api/master/**").hasAnyAuthority("MASTER", "SYSTEM_ADMIN","ADMIN")

                        .requestMatchers("/profile/**").hasAnyAuthority("CLIENT","MASTER", "SYSTEM_ADMIN","ADMIN")


                        .requestMatchers("/services.html", "/masters.html","/reviews.html","/api/masters").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/masters/**", "api/services/**" ,"/api/reviews").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/appointments/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/appointments/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/appointments/**").hasAnyAuthority("CLIENT","MASTER", "SYSTEM_ADMIN","ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/appointments/**").hasAnyAuthority("CLIENT","MASTER", "SYSTEM_ADMIN","ADMIN")

                        .anyRequest().authenticated()
                )

                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}