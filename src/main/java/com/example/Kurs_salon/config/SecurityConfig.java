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
import com.example.Kurs_salon.model.UserAuthority;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Публичные ресурсы - разрешаем доступ всем
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/services.html",
                                "/masters.html",
                                "/reviews.html",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/login.html",
                                "/register.html"
                        ).permitAll()

                        // Публичные API - разрешаем доступ всем
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/services/**",
                                "/api/masters/**",
                                "/api/reviews/**"
                        ).permitAll()

                        // Защищенные страницы
                        .requestMatchers(
                                "/profile.html",
                                "/appointments.html"
                        ).authenticated()

                        // Защищенные API
                        .requestMatchers("/api/appointments/**").hasAnyAuthority(
                                UserAuthority.CLIENT.getAuthority(),
                                UserAuthority.ADMIN.getAuthority()
                        )
                        .requestMatchers(HttpMethod.POST, "/api/reviews/**").hasAuthority(
                                UserAuthority.CLIENT.getAuthority()
                        )
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login.html")
                        .loginProcessingUrl("/api/auth/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login.html?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}