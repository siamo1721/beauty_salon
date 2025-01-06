package com.example.Kurs_salon.service;

import com.example.Kurs_salon.model.Appointment;
import com.example.Kurs_salon.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final AppointmentRepository appointmentRepository;

    public byte[] generateServicesReport(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
        LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");

        List<Appointment> appointments = appointmentRepository
                .findByAppointmentDateBetween(start, end);

        return generatePdfReport(appointments);
    }

    public byte[] generateMastersScheduleReport() {
        // Здесь должна быть логика генерации расписания мастеров
        // Например, можно получить все записи и сгруппировать их по мастерам
        List<Appointment> appointments = appointmentRepository.findAll();
        return generatePdfReport(appointments); // Используйте существующий метод для создания PDF
    }

    private byte[] generatePdfReport(List<Appointment> appointments) {
        // Реализация генерации PDF
        return new byte[0];
    }
}
