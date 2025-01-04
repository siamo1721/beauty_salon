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
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "master_id")
    private Master master;

    private LocalDateTime appointmentDate;
    private String status;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Servicee servicee;
}