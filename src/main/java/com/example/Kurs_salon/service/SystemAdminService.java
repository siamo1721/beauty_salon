package com.example.Kurs_salon.service;

import com.example.Kurs_salon.dto.AppointmentUpdateDto;
import com.example.Kurs_salon.model.Appointment;
import com.example.Kurs_salon.repository.AppointmentRepository;
import com.example.Kurs_salon.repository.MasterRepository;
import com.example.Kurs_salon.repository.ServiceRepository;
import com.example.Kurs_salon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemAdminService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final MasterRepository masterRepository;
    private final ServiceRepository serviceRepository;
    private final ReportService reportService;

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getSchedule(LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByAppointmentDateBetween(start, end);
    }

    @Transactional
    public Appointment updateAppointment(Long id, AppointmentUpdateDto updateDto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Запись не найдена"));

        appointment.setAppointmentDate(updateDto.getAppointmentDate());
        appointment.setUser(userRepository.findById(updateDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден")));
        appointment.setMaster(masterRepository.findById(updateDto.getMasterId())
                .orElseThrow(() -> new RuntimeException("Мастер не найден")));
        appointment.setServicee(serviceRepository.findById(updateDto.getServiceId())
                .orElseThrow(() -> new RuntimeException("Услуга не найдена")));

        return appointmentRepository.save(appointment);
    }

    public byte[] generateMastersScheduleReport() {
        return reportService.generateMastersScheduleReport();
    }
}