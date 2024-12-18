package com.example.Kurs_salon.model;


import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name = "services")
public class Servicee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal price;
    private Integer duration;
    private String description;
}
