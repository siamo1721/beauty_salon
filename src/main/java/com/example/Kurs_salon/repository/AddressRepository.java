package com.example.Kurs_salon.repository;

import com.example.Kurs_salon.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}