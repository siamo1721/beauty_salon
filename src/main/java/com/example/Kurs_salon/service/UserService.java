package com.example.Kurs_salon.service;

import com.example.Kurs_salon.dto.RegistrationDto;
import com.example.Kurs_salon.model.User;


import java.io.IOException;

public interface UserService {
    User getCurrentUser();
    void registerClient(RegistrationDto registrationDto) throws IOException;
}