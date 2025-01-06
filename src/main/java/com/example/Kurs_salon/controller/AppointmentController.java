package com.example.Kurs_salon.controller;

import com.example.Kurs_salon.dto.AppointmentCreateDTO;
import com.example.Kurs_salon.model.Appointment;
import com.example.Kurs_salon.model.User;
import com.example.Kurs_salon.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Slf4j
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(
             @RequestBody AppointmentCreateDTO appointmentDTO,
            @AuthenticationPrincipal User currentUser
    ) {

        return ResponseEntity.ok(appointmentService.createAppointment(appointmentDTO, currentUser));
    }
    
    @GetMapping("/master/{masterId}/schedule")
    public ResponseEntity<List<Appointment>> getMasterSchedule(
            @PathVariable Long masterId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(appointmentService.getMasterSchedule(masterId, start, end));
    }

    @GetMapping("/my")
    public ResponseEntity<List<Appointment>> getCurrentUserAppointments(
            @AuthenticationPrincipal User currentUser
    ) {
        log.info("Получен запрос на получение записей для пользователя: {}",
                currentUser != null ? currentUser.getId() : "null");
            return ResponseEntity.ok(appointmentService.getUserAppointments(currentUser.getId()));

    }



}