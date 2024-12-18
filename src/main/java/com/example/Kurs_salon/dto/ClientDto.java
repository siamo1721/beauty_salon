package com.example.Kurs_salon.dto;


import lombok.Data;
import java.time.LocalDate;

@Data
public class ClientDto {
    private UserDto user;
    private LocalDate birthDate;
    private Long addressId;
    private LocalDate registrationDate;
}
