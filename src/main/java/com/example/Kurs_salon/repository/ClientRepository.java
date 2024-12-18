package com.example.Kurs_salon.repository;
import com.example.Kurs_salon.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}