package com.example.Kurs_salon.dto;


import lombok.Data;

@Data
public class AddressDto {
    private String city;
    private String street;
    private String house;
    private String apartment;
}