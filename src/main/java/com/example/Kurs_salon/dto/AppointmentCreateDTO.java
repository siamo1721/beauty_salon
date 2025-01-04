package com.example.Kurs_salon.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentCreateDTO {
    private Long masterId;
    private Long serviceId;
    private LocalDateTime appointmentDate;
}
