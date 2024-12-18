package com.example.Kurs_salon.controller;

import com.example.Kurs_salon.model.Servicee;
import com.example.Kurs_salon.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceService serviceService;
    
    @PostMapping
    public ResponseEntity<Servicee> createService(@RequestBody Servicee servicee) {
        return ResponseEntity.ok(serviceService.createService(servicee));
    }
    
    @GetMapping
    public ResponseEntity<List<Servicee>> getAllServices() {
        return ResponseEntity.ok(serviceService.getAllServices());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Servicee> getService(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.getServiceById(id));
    }
}