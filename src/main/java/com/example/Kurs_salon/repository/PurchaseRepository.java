package com.example.Kurs_salon.repository;
import com.example.Kurs_salon.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByAppointmentId(Long appointmentId);
}