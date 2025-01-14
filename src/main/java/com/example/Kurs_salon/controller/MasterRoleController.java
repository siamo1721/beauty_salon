package com.example.Kurs_salon.controller;

import com.example.Kurs_salon.dto.AppointmentStatusUpdateDto;
import com.example.Kurs_salon.dto.MasterStatistics;
import com.example.Kurs_salon.model.Appointment;
import com.example.Kurs_salon.service.MasterRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master")
@PreAuthorize("hasAuthority('MASTER')")
@RequiredArgsConstructor
public class MasterRoleController {
    private final MasterRoleService masterRoleService;

    @GetMapping("/appointments/today")
    public ResponseEntity<List<Appointment>> getTodayAppointments() {
        return ResponseEntity.ok(masterRoleService.getTodayAppointments());
    }
    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getMasterAppointments() {
        return ResponseEntity.ok(masterRoleService.getMasterAppointments());
    }

    @PostMapping("/appointments/{id}/status")
    public ResponseEntity<Appointment> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestBody AppointmentStatusUpdateDto statusUpdate) {
        return ResponseEntity.ok(masterRoleService.updateAppointmentStatus(id, statusUpdate.getStatus()));
    }

    @GetMapping("/statistics")
    public ResponseEntity<MasterStatistics> getMasterStatistics() {
        return ResponseEntity.ok(masterRoleService.getMasterStatistics());
    }
}