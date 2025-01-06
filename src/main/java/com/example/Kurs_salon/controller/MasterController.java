package com.example.Kurs_salon.controller;

import com.example.Kurs_salon.dto.MasterDto;
import com.example.Kurs_salon.model.Master;
import com.example.Kurs_salon.model.User;
import com.example.Kurs_salon.repository.MasterRepository;
import com.example.Kurs_salon.repository.UserRepository;
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
    private final MasterRepository masterRepository;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createMaster(@RequestBody MasterDto request) {
        // Ищем пользователя по ID
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Создаем объект Master
        Master master = new Master();
        master.setUser(user);
        master.setSpecialization(request.getSpecialization());
        master.setWorkSchedule(request.getWorkSchedule());

        // Сохраняем мастера
        Master savedMaster = masterRepository.save(master);

        return ResponseEntity.ok(savedMaster);
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