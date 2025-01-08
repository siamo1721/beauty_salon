package com.example.Kurs_salon.dto;

import lombok.Data;

@Data
public class RegistrationDto {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String PhotoPath;
    private String birthDate;
    private AddressDto address;
}