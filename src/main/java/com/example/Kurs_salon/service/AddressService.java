package com.example.Kurs_salon.service;

import com.example.Kurs_salon.model.Address;
import com.example.Kurs_salon.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    
    public Address createAddress(Address address) {
        return addressRepository.save(address);
    }
    
    public Address getAddressById(Long id) {
        return addressRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Address not found"));
    }
    
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }
}