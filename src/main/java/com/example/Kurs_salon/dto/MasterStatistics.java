package com.example.Kurs_salon.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MasterStatistics {
    private int totalAppointments;
    private int completedAppointments;
    private BigDecimal totalEarnings;
    private double averageRating;
}