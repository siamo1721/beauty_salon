package com.example.Kurs_salon.repository;
import com.example.Kurs_salon.model.Master;
import com.example.Kurs_salon.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByAppointmentId(Long appointmentId);
    @Query("SELECT AVG(r.rating) FROM Review r JOIN r.appointment a WHERE a.master = :master")
    Double calculateAverageRatingForMaster(@Param("master") Master master);
}