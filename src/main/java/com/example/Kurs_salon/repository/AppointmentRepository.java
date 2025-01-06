package com.example.Kurs_salon.repository;
import com.example.Kurs_salon.model.Appointment;
import com.example.Kurs_salon.model.Master;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByMasterIdAndAppointmentDateBetween(Long masterId, LocalDateTime start, LocalDateTime end);
    List<Appointment> findByUserId(Long userId);
    List<Appointment> findByAppointmentDateBetween(LocalDateTime start, LocalDateTime end);
    List<Appointment> findByMasterId(Long masterId);
    List<Appointment> findByMasterAndAppointmentDateBetween(Master master, LocalDateTime start, LocalDateTime end);
}