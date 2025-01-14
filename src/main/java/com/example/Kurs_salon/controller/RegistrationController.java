package com.example.Kurs_salon.controller;

import com.example.Kurs_salon.dto.LoginDto;
import com.example.Kurs_salon.dto.RegistrationDto;
import com.example.Kurs_salon.model.User;
import com.example.Kurs_salon.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationDto registrationDto) throws IOException {
        userService.registerClient(registrationDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto, HttpSession session) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            session.setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext()
            );

            var authorities = authentication.getAuthorities();
            var roles = authorities.stream()
                    .map(authority -> authority.getAuthority())
                    .toList();

            return ResponseEntity.ok().body(Map.of(
                    "authenticated", true,
                    "username", authentication.getName(),
                    "roles", roles
            ));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of(
                    "authenticated", false,
                    "message", "Неверные учетные данные"
            ));
        }
    }


    @GetMapping("/check")
    public ResponseEntity<?> checkAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null &&
                auth.isAuthenticated() &&
                !(auth instanceof AnonymousAuthenticationToken);

        if (isAuthenticated) {
            return ResponseEntity.ok().body(Map.of(
                    "authenticated", true,
                    "username", auth.getName()
            ));
        } else {
            return ResponseEntity.ok().body(Map.of(
                    "authenticated", false
            ));
        }
    }

    @PostMapping("/logout")
        public ResponseEntity<?> logout() {
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok().build();
        }
    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(currentUser);
    }
    }
