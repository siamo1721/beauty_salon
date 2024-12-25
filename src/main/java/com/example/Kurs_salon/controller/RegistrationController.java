package com.example.Kurs_salon.controller;

import com.example.Kurs_salon.dto.LoginDto;
import com.example.Kurs_salon.dto.RegistrationDto;
import com.example.Kurs_salon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationDto registrationDto) {
        userService.registerClient(
                registrationDto.getUsername(),
                registrationDto.getPassword(),
                registrationDto.getFirstName(),
                registrationDto.getLastName(),
                registrationDto.getEmail(),
                registrationDto.getPhone()
        );
        return ResponseEntity.ok().build();
    }

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
            try {
                // Аутентификация через AuthenticationManager
                Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(),
                    loginDto.getPassword()
                )

            );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return ResponseEntity.ok().body(Map.of("authenticated", true));
            } catch (AuthenticationException e) {
                return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
            }
//            session.setAttribute("USER_ID", user.getId());
//            return ResponseEntity.ok().build();

        }

        @GetMapping("/check")
        public ResponseEntity<?> checkAuth() {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isAuthenticated = auth != null && auth.isAuthenticated() &&
                    !(auth instanceof AnonymousAuthenticationToken);
            return ResponseEntity.ok().body(Map.of("authenticated", isAuthenticated));
        }
        @PostMapping("/logout")
        public ResponseEntity<?> logout() {
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok().build();
        }
    }
