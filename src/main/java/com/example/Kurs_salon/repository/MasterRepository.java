package com.example.Kurs_salon.repository;
import com.example.Kurs_salon.model.Master;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MasterRepository extends JpaRepository<Master, Long> {
    Optional<Master> findByUserUsername(String username);
    Optional<Master> findByUser_FirstNameAndUser_LastName(String firstName, String lastName);
}