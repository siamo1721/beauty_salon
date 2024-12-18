package com.example.Kurs_salon.controller;

import com.example.Kurs_salon.model.Master;
import com.example.Kurs_salon.service.MasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/masters")
@RequiredArgsConstructor
public class MasterController {
    private final MasterService masterService;
    
    @PostMapping
    public ResponseEntity<Master> createMaster(@RequestBody Master master) {
        return ResponseEntity.ok(masterService.createMaster(master));
    }
    
    @GetMapping
    public ResponseEntity<List<Master>> getAllMasters() {
        return ResponseEntity.ok(masterService.getAllMasters());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Master> getMaster(@PathVariable Long id) {
        return ResponseEntity.ok(masterService.getMasterById(id));
    }
}