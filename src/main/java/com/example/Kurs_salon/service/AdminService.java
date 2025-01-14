package com.example.Kurs_salon.service;

import com.example.Kurs_salon.dto.AddressDto;
import com.example.Kurs_salon.dto.MasterDto;
import com.example.Kurs_salon.dto.ServiceDto;
import com.example.Kurs_salon.dto.UserDto;
import com.example.Kurs_salon.model.*;
import com.example.Kurs_salon.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.Kurs_salon.model.UserAuthority.MASTER;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final UserRolesRepository userRolesRepository;
    private final MasterRepository masterRepository;
    private final ServiceRepository serviceRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Transactional
    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        user.setUsername(userDto.getUsername());
        user.setPhone(userDto.getPhone());
        user.setBirthDate(userDto.getBirthDate());
        user.setRegistrationDate(userDto.getRegistrationDate());

        Address address = user.getAddress();

        if (address == null) {
            address = new Address();
        }

        address.setStreet(userDto.getAddress().getStreet());
        address.setCity(userDto.getAddress().getCity());
        address.setHouse(userDto.getAddress().getHouse());
        address.setApartment(userDto.getAddress().getApartment());

        address = addressRepository.save(address);
        user.setAddress(address);

        return convertToUserDto(userRepository.save(user));

    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public void updateUserRole(Long userId, UserAuthority newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        user.getUserRoles().clear();
        UserRole newUserRole = new UserRole(null, newRole, user);
        userRolesRepository.save(newUserRole);
    }

    public List<MasterDto> getAllMasters() {
        return masterRepository.findAll().stream()
                .map(this::convertToMasterDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public MasterDto addMaster(MasterDto masterDto ) {
        User user = userRepository.findById(masterDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        UserRole masterRole = new UserRole(null, MASTER, user);
        userRolesRepository.save(masterRole);
        Master master = new Master();
        master.setUser(user);
        master.setSpecialization(masterDto.getSpecialization());
        master.setWorkSchedule(masterDto.getWorkSchedule());
        return convertToMasterDto(masterRepository.save(master));
    }

    @Transactional
    public MasterDto updateMaster(Long masterId, MasterDto masterDto) {
        Master master = masterRepository.findById(masterId)
                .orElseThrow(() -> new RuntimeException("Мастер не найден"));
        master.setSpecialization(masterDto.getSpecialization());
        master.setWorkSchedule(masterDto.getWorkSchedule());
        return convertToMasterDto(masterRepository.save(master));
    }


    @Transactional
    public void deleteMaster(Long masterId) {
        masterRepository.deleteById(masterId);
    }

    public List<ServiceDto> getAllServices() {
        return serviceRepository.findAll().stream()
                .map(this::convertToServiceDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ServiceDto addService(ServiceDto serviceDto) {
        Servicee service = new Servicee();
        service.setName(serviceDto.getName());
        service.setPrice(serviceDto.getPrice());
        service.setDuration(serviceDto.getDuration());
        service.SetDescription(serviceDto.getDescription());
        return convertToServiceDto(serviceRepository.save(service));
    }

    @Transactional
    public ServiceDto updateService(Long serviceId, ServiceDto serviceDto) {
        Servicee service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Услуга не найдена"));
        service.setName(serviceDto.getName());
        service.setPrice(serviceDto.getPrice());
        service.setDuration(serviceDto.getDuration());
        service.SetDescription(serviceDto.getDescription());
        return convertToServiceDto(serviceRepository.save(service));
    }

    @Transactional
    public void deleteService(Long serviceId) {
        serviceRepository.deleteById(serviceId);
    }


    private UserDto convertToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getBirthDate(),
                user.getRegistrationDate(),
                user.getAddress() == null? null : convertToAddressDto(user.getAddress())

        );
    }
    private AddressDto convertToAddressDto(Address address) {
        return new AddressDto(
                address.getId(),
                address.getStreet(),
                address.getCity(),
                address.getHouse(),
                address.getApartment()
        );

    }

    private MasterDto convertToMasterDto(Master master) {
        return new MasterDto(
                master.getId(),
                master.getUser().getId(),
                master.getUser().getFirstName(),
                master.getUser().getLastName(),
                master.getSpecialization(),
                master.getWorkSchedule()
        );
    }

    private ServiceDto convertToServiceDto(Servicee service) {
        return new ServiceDto(
                service.getId(),
                service.getName(),
                service.getPrice(),
                service.getDuration(),
                service.getDescription()
        );
    }

}