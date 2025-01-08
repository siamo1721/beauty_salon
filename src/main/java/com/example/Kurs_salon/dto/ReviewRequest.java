package com.example.Kurs_salon.dto;

import lombok.*;

@Data
@Getter
@Setter
public class ReviewRequest {
    private Long appointmentId;
    private Integer rating;
    private String reviewText;

    public String getReviewText() {
        return reviewText;
    }
}
