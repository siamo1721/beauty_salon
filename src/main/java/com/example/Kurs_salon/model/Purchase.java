package com.example.Kurs_salon.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name = "purchases")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private LocalDateTime purchaseDate;
    private Integer quantity;
    private BigDecimal totalAmount;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Servicee servicee;

    @OneToOne
    @JoinColumn(name = "review_id")
    private Review review;
}
