package com.example.Kurs_salon.service;

import com.example.Kurs_salon.model.User;

public interface UserService {
    void registerClient(String username, String password, String firstName, String lastName, String email, String phone);
    void registerMaster(String username, String password, String firstName, String lastName, String email, String phone, String specialization);
    void registerAdmin(String username, String password, String firstName, String lastName, String email, String phone);
    User getCurrentUser();
}