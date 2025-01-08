package com.example.Kurs_salon.service;

import com.example.Kurs_salon.dto.AppointmentUpdateDto;
import com.example.Kurs_salon.model.Appointment;
import com.example.Kurs_salon.model.Master;
import com.example.Kurs_salon.model.Servicee;
import com.example.Kurs_salon.model.User;
import com.example.Kurs_salon.repository.AppointmentRepository;
import com.example.Kurs_salon.repository.MasterRepository;
import com.example.Kurs_salon.repository.ServiceRepository;
import com.example.Kurs_salon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemAdminService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final MasterRepository masterRepository;
    private final ServiceRepository serviceRepository;

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getSchedule(LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByAppointmentDateBetween(start, end);
    }

    @Transactional
    public Appointment updateAppointment(Long id, AppointmentUpdateDto updateDto) {
        System.out.println("Получен DTO: " + updateDto);

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Запись не найдена"));

        System.out.println("Найдена запись: " + appointment);

        appointment.setAppointmentDate(updateDto.getAppointmentDate());

        User user = userRepository.findByFirstNameAndLastName(
                updateDto.getUserFirstName(),
                updateDto.getUserLastName()
        ).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        System.out.println("Найден пользователь: " + user);
        appointment.setUser(user);

        Master master = masterRepository.findByUser_FirstNameAndUser_LastName(
                updateDto.getMasterFirstName(),
                updateDto.getMasterLastName()
        ).orElseThrow(() -> new RuntimeException("Мастер не найден"));
        System.out.println("Найден мастер: " + master);
        appointment.setMaster(master);

        Servicee service = serviceRepository.findByName(updateDto.getServiceName())
                .orElseThrow(() -> new RuntimeException("Услуга не найдена"));
        System.out.println("Найдена услуга: " + service);
        appointment.setServicee(service);

        return appointmentRepository.save(appointment);
    }



}