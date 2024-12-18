package com.example.Kurs_salon.repository;
import com.example.Kurs_salon.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByAppointmentId(Long appointmentId);
}