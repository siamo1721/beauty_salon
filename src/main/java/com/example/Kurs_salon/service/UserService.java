package com.example.Kurs_salon.service;

import com.example.Kurs_salon.dto.RegistrationDto;
import com.example.Kurs_salon.model.User;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    //    void registerClient(String username, String password, String firstName, String lastName, String email, String phone);
    void registerMaster(String username, String password, String firstName, String lastName, String email, String phone, String specialization);
    void registerAdmin(String username, String password, String firstName, String lastName, String email, String phone);
    User getCurrentUser();

    void registerClient(RegistrationDto registrationDto) throws IOException;
//    void updateUserPhoto(Long userId, MultipartFile file) throws IOException;
}