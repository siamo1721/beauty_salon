package com.example.Kurs_salon.repository;
import com.example.Kurs_salon.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}