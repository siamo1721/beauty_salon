package com.example.Kurs_salon.service;

import com.example.Kurs_salon.model.Appointment;
import com.example.Kurs_salon.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    
    public Appointment createAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }
    
    public List<Appointment> getMasterSchedule(Long masterId, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByMasterIdAndAppointmentDateBetween(masterId, start, end);
    }
    
    public List<Appointment> getClientAppointments(Long clientId) {
        return appointmentRepository.findByClientId(clientId);
    }
}