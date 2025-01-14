package com.example.Kurs_salon.service;

import com.example.Kurs_salon.dto.RegistrationDto;
import com.example.Kurs_salon.exception.UsernameAlreadyExistsException;
import com.example.Kurs_salon.model.Address;
import com.example.Kurs_salon.model.User;
import com.example.Kurs_salon.model.UserAuthority;
import com.example.Kurs_salon.model.UserRole;
import com.example.Kurs_salon.repository.AddressRepository;
import com.example.Kurs_salon.repository.UserRepository;
import com.example.Kurs_salon.repository.UserRolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {


    private final UserRolesRepository userRolesRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;

    public void registerClient(RegistrationDto registrationDto) throws IOException {
        if (userRepository.findByUsername(registrationDto.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException();
        }

        Address address = new Address();
        address.setStreet(registrationDto.getAddress().getStreet());
        address.setCity(registrationDto.getAddress().getCity());
        address.setHouse(registrationDto.getAddress().getHouse());
        address.setApartment(registrationDto.getAddress().getApartment());

        address = addressRepository.save(address);

        User user = new User()
                .setUsername(registrationDto.getUsername())
                .setPassword(passwordEncoder.encode(registrationDto.getPassword()))
                .setFirstName(registrationDto.getFirstName())
                .setLastName(registrationDto.getLastName())
                .setEmail(registrationDto.getEmail())
                .setPhone(registrationDto.getPhone())
                .setBirthDate(LocalDate.parse(registrationDto.getBirthDate()))
                .setAddress(address)
                .setRegistrationDate(LocalDate.now())
                .setLocked(false)
                .setExpired(false)
                .setEnabled(true);

        user = userRepository.save(user);
        userRolesRepository.save(new UserRole(null, UserAuthority.CLIENT, user));
    }
    

    @Override
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return userRepository.findByUsername(((UserDetails) principal).getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        }
        throw new UsernameNotFoundException("User not found");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}