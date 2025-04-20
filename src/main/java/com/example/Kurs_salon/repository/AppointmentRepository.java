package com.example.Kurs_salon.repository;
import com.example.Kurs_salon.model.Appointment;
import com.example.Kurs_salon.model.Master;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    int countByMaster(Master master);

    int countByMasterAndStatus(Master master, String status);
    @Query("SELECT DISTINCT a FROM Appointment a " +
            "JOIN FETCH a.user u " +
            "JOIN FETCH a.servicee s " +
            "JOIN FETCH a.master m " +
            "JOIN FETCH m.user mu ")
    List<Appointment> findAllAppointmentsWithDetails();

    @Query("SELECT SUM(s.price) FROM Appointment a JOIN a.servicee s WHERE a.master = :master AND a.status = 'COMPLETED'")
    BigDecimal sumEarningsByMaster(@Param("master") Master master);
    List<Appointment> findByMasterIdAndAppointmentDateBetween(Long masterId, LocalDateTime start, LocalDateTime end);
    List<Appointment> findByUserId(Long userId);
    List<Appointment> findByAppointmentDateBetween(LocalDateTime start, LocalDateTime end);
    List<Appointment> findByMasterId(Long masterId);
    List<Appointment> findByMasterAndAppointmentDateBetween(Master master, LocalDateTime start, LocalDateTime end);
    List<Appointment> findAllByAppointmentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}