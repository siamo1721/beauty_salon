package com.example.Kurs_salon.controller;

import com.example.Kurs_salon.dto.AppointmentUpdateDto;
import com.example.Kurs_salon.model.Appointment;
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

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(systemAdminService.getAllAppointments());
    }

    @PutMapping("/appointments/{id}")
    public ResponseEntity<Appointment> updateAppointment(
            @PathVariable Long id,
            @RequestBody AppointmentUpdateDto updateDto) {
        return ResponseEntity.ok(systemAdminService.updateAppointment(id, updateDto));
    }

    // Оставляем существующие методы
    @GetMapping("/schedule")
    public ResponseEntity<List<Appointment>> getSchedule(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(systemAdminService.getSchedule(start, end));
    }

    @GetMapping("/masters/schedule")
    public ResponseEntity<byte[]> getMastersScheduleReport() {
        byte[] report = systemAdminService.generateMastersScheduleReport();
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=masters-schedule.pdf")
                .body(report);
    }
}