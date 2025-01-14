package com.example.Kurs_salon.controller;

import com.example.Kurs_salon.dto.ReviewRequest;
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

    @PostMapping("/create")
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest reviewRequest) {
        Review review = new Review();
        review.setRating(reviewRequest.getRating());
        review.setReviewText(reviewRequest.getReviewText());

        Review createdReview = reviewService.createReview(reviewRequest.getAppointmentId(), review);
        return ResponseEntity.ok(createdReview);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReview(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<Review>> getReviewsByAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(reviewService.getReviewsByAppointment(appointmentId));
    }
}