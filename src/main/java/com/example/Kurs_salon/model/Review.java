package com.example.Kurs_salon.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer rating;
    private String reviewText;
    private LocalDateTime reviewDate;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
}
