package com.example.Kurs_salon.controller;

import com.example.Kurs_salon.dto.AppointmentUpdateDto;
import com.example.Kurs_salon.model.Appointment;
import com.example.Kurs_salon.model.Master;
import com.example.Kurs_salon.model.Servicee;
import com.example.Kurs_salon.model.User;
import com.example.Kurs_salon.repository.AppointmentRepository;
import com.example.Kurs_salon.repository.MasterRepository;
import com.example.Kurs_salon.repository.ServiceRepository;
import com.example.Kurs_salon.repository.UserRepository;
import com.example.Kurs_salon.service.PdfGenerationService;
import com.example.Kurs_salon.service.SystemAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/system-admin")
@PreAuthorize("hasAuthority('SYSTEM_ADMIN')")
@RequiredArgsConstructor
public class SystemAdminController {
    private final SystemAdminService systemAdminService;
    private final PdfGenerationService pdfGenerationService;
    private final ServiceRepository serviceRepository;
    private final MasterRepository masterRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(systemAdminService.getAllAppointments());
    }
    @GetMapping("/appointments/{id}")
    public ResponseEntity<Appointment> getAppointment(@PathVariable Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Запись не найдена"));
        return ResponseEntity.ok(appointment);
    }


    @PutMapping("/appointments/{id}")
    public ResponseEntity<Appointment> updateAppointment(
            @PathVariable Long id,
            @RequestBody AppointmentUpdateDto updateDto) {
        return ResponseEntity.ok(systemAdminService.updateAppointment(id, updateDto));
    }

    @GetMapping("/schedule")
    public ResponseEntity<List<Appointment>> getSchedule(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(systemAdminService.getSchedule(start, end));
    }

    @GetMapping("/reports/appointment")
    public ResponseEntity<byte[]> getAppointmentForm(@RequestParam Long appointmentId) {
        byte[] report = pdfGenerationService.generateAppointmentForm(appointmentId);
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=appointment-form.pdf")
                .body(report);
    }

    @GetMapping("/services")
    public List<Servicee> getAllServices() {
        return serviceRepository.findAll();
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/masters")
    public List<Master> getAllMasters() {
        return masterRepository.findAll();
    }
}