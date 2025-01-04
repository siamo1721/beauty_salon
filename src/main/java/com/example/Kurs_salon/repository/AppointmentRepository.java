package com.example.Kurs_salon.repository;
import com.example.Kurs_salon.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByMasterIdAndAppointmentDateBetween(Long masterId, LocalDateTime start, LocalDateTime end);
    List<Appointment> findByUserId(Long userId);
}