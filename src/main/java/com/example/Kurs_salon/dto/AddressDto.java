package com.example.Kurs_salon.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
public class AddressDto {
    private Long id;
    private String city;
    private String street;
    private String house;
    private String apartment;

}