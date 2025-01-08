package com.example.Kurs_salon.repository;
import com.example.Kurs_salon.model.Servicee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Servicee, Long> {
    Optional<Servicee> findByName(String name);
}