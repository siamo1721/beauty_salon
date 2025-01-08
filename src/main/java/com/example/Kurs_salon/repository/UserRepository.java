package com.example.Kurs_salon.repository;
import com.example.Kurs_salon.model.User;
import com.example.Kurs_salon.model.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    @Query("SELECT u FROM users u JOIN u.userRoles r WHERE r.userAuthority = :role")
    List<User> findAllByRole(@Param("role") UserAuthority role);
    Optional<User> findByFirstNameAndLastName(String firstName, String lastName);
}