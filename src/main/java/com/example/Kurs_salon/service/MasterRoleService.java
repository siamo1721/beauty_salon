package com.example.Kurs_salon.service;

import com.example.Kurs_salon.dto.MasterStatistics;
import com.example.Kurs_salon.model.Appointment;
import com.example.Kurs_salon.model.Master;
import com.example.Kurs_salon.model.User;
import com.example.Kurs_salon.repository.AppointmentRepository;
import com.example.Kurs_salon.repository.MasterRepository;
import com.example.Kurs_salon.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContext;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MasterRoleService {
    private final AppointmentRepository appointmentRepository;
    private final MasterRepository masterRepository;
    private final UserRepository userRepository;

    public List<Appointment> getTodayAppointments() {
        Master currentMaster = getCurrentMaster();
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);

        return appointmentRepository.findByMasterAndAppointmentDateBetween(
                currentMaster, startOfDay, endOfDay);
    }
    public List<Appointment> getMasterAppointments() {
        Master currentMaster = getCurrentMaster(); // Метод для получения текущего пользователя
        return appointmentRepository.findByMasterId(currentMaster.getId());
    }
    private User getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("No authenticated user found");
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Transactional
    public Appointment updateAppointmentStatus(Long id, String status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Запись не найдена"));

        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    public MasterStatistics getMasterStatistics() {
        Master currentMaster = getCurrentMaster();
        // Реализация подсчета статистики
        return new MasterStatistics();
    }

    private Master getCurrentMaster() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return masterRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Мастер не найден"));
    }
}