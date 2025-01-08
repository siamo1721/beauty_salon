package com.example.Kurs_salon.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentUpdateDto {
    private LocalDateTime appointmentDate;
    private String userFirstName;
    private String userLastName;
    private String masterFirstName;
    private String masterLastName;
    private String serviceName;
}
