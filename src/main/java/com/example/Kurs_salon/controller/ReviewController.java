package com.example.Kurs_salon.controller;

import com.example.Kurs_salon.model.Review;
import com.example.Kurs_salon.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    
    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        return ResponseEntity.ok(reviewService.createReview(review));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReview(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }
    
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<Review>> getReviewsByAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(reviewService.getReviewsByAppointment(appointmentId));
    }
}