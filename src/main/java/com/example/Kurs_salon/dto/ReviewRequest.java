package com.example.Kurs_salon.dto;

import lombok.Data;

@Data
public class ReviewRequest {
    private Long appointmentId;
    private Integer rating;
    private String reviewText;
}
