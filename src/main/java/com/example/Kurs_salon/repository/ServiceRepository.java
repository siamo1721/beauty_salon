package com.example.Kurs_salon.repository;
import com.example.Kurs_salon.model.Servicee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Servicee, Long> {
}