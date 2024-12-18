package com.example.Kurs_salon.service;

import com.example.Kurs_salon.model.Purchase;
import com.example.Kurs_salon.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    
    public Purchase createPurchase(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }
    
    public Purchase getPurchaseById(Long id) {
        return purchaseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Purchase not found"));
    }
    
    public List<Purchase> getPurchasesByAppointment(Long appointmentId) {
        return purchaseRepository.findByAppointmentId(appointmentId);
    }
}