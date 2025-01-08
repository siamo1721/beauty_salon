package com.example.Kurs_salon.service;

import com.example.Kurs_salon.model.Appointment;
import com.example.Kurs_salon.model.User;
import com.example.Kurs_salon.model.UserAuthority;
import com.example.Kurs_salon.repository.AppointmentRepository;
import com.example.Kurs_salon.repository.UserRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfGenerationService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public byte[] generateProceduresReport(LocalDateTime startDate, LocalDateTime endDate) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            document.add(new Paragraph("Отчет по выполненным процедурам"));
            document.add(new Paragraph("Период: " + startDate + " - " + endDate));

            List<Appointment> appointments = appointmentRepository.findAllByAppointmentDateBetween(startDate, endDate);
            for (Appointment appointment : appointments) {
                document.add(new Paragraph("Дата: " + appointment.getAppointmentDate()));
                document.add(new Paragraph("Клиент: " + appointment.getUser().getLastName()));
                document.add(new Paragraph("Услуга: " + appointment.getServicee().getName()));
                document.add(new Paragraph("Статус: " + appointment.getStatus()));
                document.add(new Paragraph("---"));
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        return baos.toByteArray();
    }

    public byte[] generateClientsReport() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            document.add(new Paragraph("Список клиентов с их записями"));

            List<User> clients = userRepository.findAllByRole(UserAuthority.CLIENT);
            for (User client : clients) {
                document.add(new Paragraph("Клиент: " + client.getFirstName()));
                List<Appointment> appointments = appointmentRepository.findAllByUser(client);
                for (Appointment appointment : appointments) {
                    document.add(new Paragraph("  Дата: " + appointment.getAppointmentDate()));
                    document.add(new Paragraph("  Услуга: " + appointment.getServicee().getName()));
                    document.add(new Paragraph("  Статус: " + appointment.getStatus()));
                }
                document.add(new Paragraph("---"));
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        return baos.toByteArray();
    }

    public byte[] generateAppointmentForm(Long appointmentId) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            document.add(new Paragraph("Форма записи на процедуру"));

            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new RuntimeException("Запись не найдена"));

            document.add(new Paragraph("Дата и время: " + appointment.getAppointmentDate()));
            document.add(new Paragraph("Клиент: " + appointment.getUser().getFirstName()));
            document.add(new Paragraph("Услуга: " + appointment.getServicee().getName()));
            document.add(new Paragraph("Мастер: " + appointment.getMaster().getUser().getFirstName()));
            document.add(new Paragraph("Статус: " + appointment.getStatus()));
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        return baos.toByteArray();
    }
}