package com.example.Kurs_salon.model;


import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum UserAuthority implements GrantedAuthority {
    ADMIN,
    SYSTEM_ADMIN,
    MASTER,
    CLIENT;

    @Override
    public String getAuthority() {
        return this.name();
    }
}