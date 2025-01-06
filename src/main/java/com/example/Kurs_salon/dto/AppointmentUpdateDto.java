package com.example.Kurs_salon.dto;

import com.example.Kurs_salon.model.Master;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentUpdateDto {
    private LocalDateTime appointmentDate;
    private Long serviceId;
    private Long userId;
    private Long masterId;
}