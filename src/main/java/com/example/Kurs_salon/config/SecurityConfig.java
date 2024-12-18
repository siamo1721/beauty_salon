package com.example.Kurs_salon.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.example.Kurs_salon.model.UserAuthority;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()

                        // Клиентские эндпоинты
                        .requestMatchers("/api/clients/**").hasAnyAuthority(
                                UserAuthority.ADMIN.getAuthority(),
                                UserAuthority.SYSTEM_ADMIN.getAuthority())

                        // Мастера
                        .requestMatchers("/api/masters/**").hasAnyAuthority(
                                UserAuthority.ADMIN.getAuthority(),
                                UserAuthority.SYSTEM_ADMIN.getAuthority())

                        // Услуги
                        .requestMatchers(HttpMethod.GET, "/api/services/**").permitAll()
                        .requestMatchers("/api/services/**").hasAnyAuthority(
                                UserAuthority.ADMIN.getAuthority(),
                                UserAuthority.SYSTEM_ADMIN.getAuthority())

                        // Записи
                        .requestMatchers(HttpMethod.POST, "/api/appointments/**").hasAnyAuthority(
                                UserAuthority.CLIENT.getAuthority(),
                                UserAuthority.ADMIN.getAuthority())
                        .requestMatchers("/api/appointments/**").hasAnyAuthority(
                                UserAuthority.COSMETOLOGIST.getAuthority(),
                                UserAuthority.ADMIN.getAuthority())

                        // Отзывы
                        .requestMatchers(HttpMethod.POST, "/api/reviews/**").hasAuthority(
                                UserAuthority.CLIENT.getAuthority())
                        .requestMatchers("/api/reviews/**").permitAll()

                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
