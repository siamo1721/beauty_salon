package com.example.Kurs_salon.service;

import com.example.Kurs_salon.model.Servicee;
import com.example.Kurs_salon.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceService {
    private final ServiceRepository serviceRepository;
    
    public Servicee createService(Servicee servicee) {
        return serviceRepository.save(servicee);
    }
    
    public List<Servicee> getAllServices() {
        return serviceRepository.findAll();
    }
    
    public Servicee getServiceById(Long id) {
        return serviceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Service not found"));
    }
}