package com.example.Kurs_salon.controller;

import com.example.Kurs_salon.model.Purchase;
import com.example.Kurs_salon.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;
    
    @PostMapping
    public ResponseEntity<Purchase> createPurchase(@RequestBody Purchase purchase) {
        return ResponseEntity.ok(purchaseService.createPurchase(purchase));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getPurchase(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseService.getPurchaseById(id));
    }
    
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<Purchase>> getPurchasesByAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(purchaseService.getPurchasesByAppointment(appointmentId));
    }
}