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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
                .setPhotoPath(registrationDto.getPhotoPath())
                .setAddress(address)
                .setRegistrationDate(LocalDate.now())
                .setLocked(false)
                .setExpired(false)
                .setEnabled(true);

        user = userRepository.save(user);
        userRolesRepository.save(new UserRole(null, UserAuthority.CLIENT, user));
    }

    @Transactional
    @Override
    public void registerMaster(String username, String password, String firstName, String lastName, String email, String phone, String specialization) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException();
        }

        User user = new User()
                .setUsername(username)
                .setPassword(passwordEncoder.encode(password))
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setPhone(phone)
                .setLocked(false)
                .setExpired(false)
                .setEnabled(true);

        user = userRepository.save(user);
        userRolesRepository.save(new UserRole(null, UserAuthority.MASTER, user));
    }

    @Transactional
    @Override
    public void registerAdmin(String username, String password, String firstName, String lastName, String email, String phone) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException();
        }

        User user = new User()
                .setUsername(username)
                .setPassword(passwordEncoder.encode(password))
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setPhone(phone)
                .setLocked(false)
                .setExpired(false)
                .setEnabled(true);

        user = userRepository.save(user);
        userRolesRepository.save(new UserRole(null, UserAuthority.ADMIN, user));
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