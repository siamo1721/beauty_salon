package com.example.Kurs_salon.controller;

import com.example.Kurs_salon.model.Appointment;
import com.example.Kurs_salon.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;
    
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        return ResponseEntity.ok(appointmentService.createAppointment(appointment));
    }
    
    @GetMapping("/master/{masterId}/schedule")
    public ResponseEntity<List<Appointment>> getMasterSchedule(
            @PathVariable Long masterId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(appointmentService.getMasterSchedule(masterId, start, end));
    }
    
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Appointment>> getClientAppointments(@PathVariable Long clientId) {
        return ResponseEntity.ok(appointmentService.getClientAppointments(clientId));
    }
}