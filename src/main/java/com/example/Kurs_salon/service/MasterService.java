package com.example.Kurs_salon.service;

import com.example.Kurs_salon.model.Master;
import com.example.Kurs_salon.repository.MasterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MasterService {
    private final MasterRepository masterRepository;
    
    public Master createMaster(Master master) {
        return masterRepository.save(master);
    }
    
    public List<Master> getAllMasters() {
        return masterRepository.findAll();
    }
    
    public Master getMasterById(Long id) {
        return masterRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Master not found"));
    }
}