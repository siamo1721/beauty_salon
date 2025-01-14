package com.example.Kurs_salon.service;

import com.example.Kurs_salon.model.Appointment;
import com.example.Kurs_salon.model.Review;
import com.example.Kurs_salon.repository.AppointmentRepository;
import com.example.Kurs_salon.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final AppointmentRepository appointmentRepository;

    public Review createReview(Long appointmentId, Review review) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Запись не найдена"));

        if (!"COMPLETED".equals(appointment.getStatus())) {
            throw new RuntimeException("Отзыв можно оставить только для завершенной записи");
        }

        review.setAppointment(appointment);
        review.setReviewDate(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
    }

    public List<Review> getReviewsByAppointment(Long appointmentId) {
        return reviewRepository.findByAppointmentId(appointmentId);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }
}