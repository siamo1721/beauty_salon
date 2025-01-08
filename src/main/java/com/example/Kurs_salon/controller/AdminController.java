package com.example.Kurs_salon.controller;

import com.example.Kurs_salon.dto.MasterDto;
import com.example.Kurs_salon.dto.ServiceDto;
import com.example.Kurs_salon.dto.UserDto;
import com.example.Kurs_salon.dto.UserRoleUpdateDto;
import com.example.Kurs_salon.model.User;
import com.example.Kurs_salon.model.UserAuthority;
import com.example.Kurs_salon.service.AdminService;
import com.example.Kurs_salon.service.PdfGenerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final PdfGenerationService pdfGenerationService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }


    @PostMapping("/users/{userId}/role")
    public ResponseEntity<Void> updateUserRole(@PathVariable Long userId, @RequestBody UserRoleUpdateDto roleUpdate) {
        UserAuthority authority = UserAuthority.valueOf(roleUpdate.getRole().toUpperCase());
        adminService.updateUserRole(userId, authority);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/users/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(adminService.updateUser(userId, userDto));
    }
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/masters")
    public ResponseEntity<List<MasterDto>> getAllMasters() {
        return ResponseEntity.ok(adminService.getAllMasters());
    }

    @PostMapping("/masters")
    public ResponseEntity<MasterDto> addMaster(@RequestBody MasterDto masterDto) {
        return ResponseEntity.ok(adminService.addMaster(masterDto));
    }

    @PutMapping("/masters/{masterId}")
    public ResponseEntity<MasterDto> updateMaster(@PathVariable Long masterId, @RequestBody MasterDto masterDto) {
        return ResponseEntity.ok(adminService.updateMaster(masterId, masterDto));
    }
    @DeleteMapping("/masters/{masterId}")
    public ResponseEntity<Void> deleteMaster(@PathVariable Long masterId) {
        adminService.deleteMaster(masterId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/services")
    public ResponseEntity<List<ServiceDto>> getAllServices() {
        return ResponseEntity.ok(adminService.getAllServices());
    }

    @PostMapping("/services")
    public ResponseEntity<ServiceDto> addService(@RequestBody ServiceDto serviceDto) {
        return ResponseEntity.ok(adminService.addService(serviceDto));
    }

    @PutMapping("/services/{serviceId}")
    public ResponseEntity<ServiceDto> updateService(@PathVariable Long serviceId, @RequestBody ServiceDto serviceDto) {
        return ResponseEntity.ok(adminService.updateService(serviceId, serviceDto));
    }

    @DeleteMapping("/services/{serviceId}")
    public ResponseEntity<Void> deleteService(@PathVariable Long serviceId) {
        adminService.deleteService(serviceId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reports/procedures")
    public ResponseEntity<byte[]> getProceduresReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("Received request for procedures report. Start date: {}, End date: {}", startDate, endDate);
        try {
            byte[] report = pdfGenerationService.generateProceduresReport(startDate, endDate);
            log.info("Report generated successfully. Size: {} bytes", report.length);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/pdf")
                    .header("Content-Disposition", "attachment; filename=procedures-report.pdf")
                    .body(report);
        } catch (Exception e) {
            log.error("Error generating procedures report", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/reports/clients")
    public ResponseEntity<byte[]> getClientsReport() {
        byte[] report = pdfGenerationService.generateClientsReport();
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=clients-report.pdf")
                .body(report);
    }
}