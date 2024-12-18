package com.example.Kurs_salon.model;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate birthDate;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    private LocalDate registrationDate;
}