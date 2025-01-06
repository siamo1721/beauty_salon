package com.example.Kurs_salon.service;

import com.example.Kurs_salon.dto.MasterDto;
import com.example.Kurs_salon.dto.ServiceDto;
import com.example.Kurs_salon.dto.UserDto;
import com.example.Kurs_salon.model.*;
import com.example.Kurs_salon.repository.MasterRepository;
import com.example.Kurs_salon.repository.ServiceRepository;
import com.example.Kurs_salon.repository.UserRepository;
import com.example.Kurs_salon.repository.UserRolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final UserRolesRepository userRolesRepository;
    private final MasterRepository masterRepository;
    private final ServiceRepository serviceRepository;
    private final ReportService reportService;

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
            user.setPassword(userDto.getPassword()); // Removed passwordEncoder
        }
        return convertToUserDto(userRepository.save(user)); // Changed to a generic convert method
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
    public MasterDto addMaster(MasterDto masterDto) {
        User user = userRepository.findById(masterDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Master master = new Master();
        master.setUser(user);
        master.setSpecialization(masterDto.getSpecialization());
        return convertToMasterDto(masterRepository.save(master));
    }

    @Transactional
    public MasterDto updateMaster(Long masterId, MasterDto masterDto) {
        Master master = masterRepository.findById(masterId)
                .orElseThrow(() -> new RuntimeException("Мастер не найден"));
        master.setSpecialization(masterDto.getSpecialization());
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
        return convertToServiceDto(serviceRepository.save(service));
    }

    @Transactional
    public ServiceDto updateService(Long serviceId, ServiceDto serviceDto) {
        Servicee service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Услуга не найдена"));
        service.setName(serviceDto.getName());
        service.setPrice(serviceDto.getPrice());
        service.setDuration(serviceDto.getDuration());
        return convertToServiceDto(serviceRepository.save(service));
    }

    @Transactional
    public void deleteService(Long serviceId) {
        serviceRepository.deleteById(serviceId);
    }

    public byte[] generateServicesReport(String startDate, String endDate) {
        return reportService.generateServicesReport(startDate, endDate);
    }
    private UserDto convertToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()

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
                service.getDuration()
        );
    }

}