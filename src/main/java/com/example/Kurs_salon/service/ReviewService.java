package com.example.Kurs_salon.service;

import com.example.Kurs_salon.model.Review;
import com.example.Kurs_salon.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }
    
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Review not found"));
    }
    
    public List<Review> getReviewsByAppointment(Long appointmentId) {
        return reviewRepository.findByAppointmentId(appointmentId);
    }
}