package com.example.Kurs_salon.service;

import com.example.Kurs_salon.dto.ReviewRequest;
import com.example.Kurs_salon.model.Appointment;
import com.example.Kurs_salon.model.Review;
import com.example.Kurs_salon.model.User;
import com.example.Kurs_salon.model.UserAuthority;
import com.example.Kurs_salon.repository.AppointmentRepository;
import com.example.Kurs_salon.repository.ReviewRepository;
import com.example.Kurs_salon.repository.UserRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;


import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PdfGenerationService {
    private final AppointmentRepository appointmentRepository;
    private final ReviewRepository reviewRepository;

    private static final Font HEADER_FONT = FontFactory.getFont("/fonts/arial.ttf", "CP1251", true, 16, Font.BOLD);
    private static final Font TITLE_FONT = FontFactory.getFont("/fonts/arial.ttf", "CP1251", true, 14, Font.BOLD);
    private static final Font NORMAL_FONT = FontFactory.getFont("/fonts/arial.ttf", "CP1251", true, 12, Font.NORMAL);

    private PdfPCell createCell(String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    public byte[] generateProceduresReport(LocalDateTime startDate, LocalDateTime endDate) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate()); // Изменено на альбомную ориентацию
        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // Заголовок
            Paragraph title = new Paragraph("Отчет по выполненным процедурам", HEADER_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Период
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            Paragraph period = new Paragraph(
                    "Период: " + startDate.format(formatter) + " - " + endDate.format(formatter),
                    NORMAL_FONT
            );
            period.setAlignment(Element.ALIGN_CENTER);
            document.add(period);
            document.add(Chunk.NEWLINE);

            // Таблица
            PdfPTable table = new PdfPTable(6); // 6 колонок
            table.setWidthPercentage(100);

            // Заголовки таблицы
            table.addCell(createCell("Дата", TITLE_FONT));
            table.addCell(createCell("Время", TITLE_FONT));
            table.addCell(createCell("Клиент", TITLE_FONT));
            table.addCell(createCell("Услуга", TITLE_FONT));
            table.addCell(createCell("Статус", TITLE_FONT));
            table.addCell(createCell("Отзыв", TITLE_FONT));

            List<Appointment> appointments = appointmentRepository.findAllByAppointmentDateBetween(startDate, endDate);
            log.info("Found {} appointments", appointments.size());
            for (Appointment appointment : appointments) {
                table.addCell(createCell(
                        appointment.getAppointmentDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                        NORMAL_FONT
                ));
                table.addCell(createCell(
                        appointment.getAppointmentDate().format(DateTimeFormatter.ofPattern("HH:mm")),
                        NORMAL_FONT
                ));
                table.addCell(createCell(
                        appointment.getUser().getLastName() + " " + appointment.getUser().getFirstName(),
                        NORMAL_FONT
                ));
                table.addCell(createCell(appointment.getServicee().getName(), NORMAL_FONT));
                table.addCell(createCell(appointment.getStatus(), NORMAL_FONT));

                // Исправляем эту часть
                List<Review> reviews = reviewRepository.findByAppointmentId(appointment.getId());
                if (!reviews.isEmpty()) {
                    String reviewTexts = reviews.stream()
                            .map(Review::getReviewText)
                            .collect(Collectors.joining(", "));
                    table.addCell(createCell(reviewTexts, NORMAL_FONT));
                } else {
                    table.addCell(createCell("Нет отзыва", NORMAL_FONT));
                }
            }

            document.add(table);

        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        return baos.toByteArray();
    }

    public byte[] generateClientsReport() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());
        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // Заголовок
            Paragraph title = new Paragraph("Список клиентов с их записями", HEADER_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Таблица
            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);

            // Заголовки
            table.addCell(createCell("Фамилия", TITLE_FONT));
            table.addCell(createCell("Имя", TITLE_FONT));
            table.addCell(createCell("Телефон", TITLE_FONT));
            table.addCell(createCell("Email", TITLE_FONT));
            table.addCell(createCell("Дата записи", TITLE_FONT));
            table.addCell(createCell("Услуга", TITLE_FONT));
            table.addCell(createCell("Мастер", TITLE_FONT));
            table.addCell(createCell("Статус", TITLE_FONT));

            // Получаем все записи с деталями
            List<Appointment> appointments = appointmentRepository.findAllAppointmentsWithDetails();

            for (Appointment appointment : appointments) {
                User client = appointment.getUser();

                table.addCell(createCell(client.getLastName(), NORMAL_FONT));
                table.addCell(createCell(client.getFirstName(), NORMAL_FONT));
                table.addCell(createCell(client.getPhone(), NORMAL_FONT));
                table.addCell(createCell(client.getEmail(), NORMAL_FONT));
                table.addCell(createCell(
                        appointment.getAppointmentDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                        NORMAL_FONT
                ));
                table.addCell(createCell(appointment.getServicee().getName(), NORMAL_FONT));
                table.addCell(createCell(
                        appointment.getMaster().getUser().getLastName() + " " +
                                appointment.getMaster().getUser().getFirstName(),
                        NORMAL_FONT
                ));
                table.addCell(createCell(appointment.getStatus(), NORMAL_FONT));
            }

            document.add(table);

        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        return baos.toByteArray();
    }


//    // Вспомогательный метод для добавления пустой строки клиента
//    private void addEmptyClientRow(PdfPTable table, User client) {
//        table.addCell(createCell(client.getLastName(), NORMAL_FONT));
//        table.addCell(createCell(client.getFirstName(), NORMAL_FONT));
//        table.addCell(createCell(client.getPhone(), NORMAL_FONT));
//        table.addCell(createCell(client.getEmail(), NORMAL_FONT));
//        table.addCell(createCell("-", NORMAL_FONT));
//        table.addCell(createCell("-", NORMAL_FONT));
//        table.addCell(createCell("-", NORMAL_FONT));
//        table.addCell(createCell("-", NORMAL_FONT));
//    }

//    // Вспомогательный метод для добавления строки с записью клиента
//    private void addClientAppointmentRow(PdfPTable table, User client, Appointment appointment, boolean includeClientInfo) {
//        if (includeClientInfo) {
//            table.addCell(createCell(client.getLastName(), NORMAL_FONT));
//            table.addCell(createCell(client.getFirstName(), NORMAL_FONT));
//            table.addCell(createCell(client.getPhone(), NORMAL_FONT));
//            table.addCell(createCell(client.getEmail(), NORMAL_FONT));
//        } else {
//            table.addCell(createCell("", NORMAL_FONT));
//            table.addCell(createCell("", NORMAL_FONT));
//            table.addCell(createCell("", NORMAL_FONT));
//            table.addCell(createCell("", NORMAL_FONT));
//        }
//
//        table.addCell(createCell(
//                appointment.getAppointmentDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
//                NORMAL_FONT
//        ));
//        table.addCell(createCell(appointment.getServicee().getName(), NORMAL_FONT));
//        table.addCell(createCell(
//                appointment.getMaster().getUser().getLastName() + " " +
//                        appointment.getMaster().getUser().getFirstName(),
//                NORMAL_FONT
//        ));
//        table.addCell(createCell(appointment.getStatus(), NORMAL_FONT));
//    }


    public byte[] generateAppointmentForm(Long appointmentId) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new RuntimeException("Запись не найдена"));

            // Заголовок
            Paragraph title = new Paragraph("Форма записи на процедуру №" + appointmentId, HEADER_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Информация о записи
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            // Добавляем строки в таблицу
            table.addCell(createCell("Дата и время:", TITLE_FONT));
            table.addCell(createCell(
                    appointment.getAppointmentDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                    NORMAL_FONT
            ));

            table.addCell(createCell("Клиент:", TITLE_FONT));
            table.addCell(createCell(
                    appointment.getUser().getLastName() + " " + appointment.getUser().getFirstName(),
                    NORMAL_FONT
            ));

            table.addCell(createCell("Услуга:", TITLE_FONT));
            table.addCell(createCell(appointment.getServicee().getName(), NORMAL_FONT));

            table.addCell(createCell("Мастер:", TITLE_FONT));
            table.addCell(createCell(
                    appointment.getMaster().getUser().getLastName() + " " +
                            appointment.getMaster().getUser().getFirstName(),
                    NORMAL_FONT
            ));

            table.addCell(createCell("Статус:", TITLE_FONT));
            table.addCell(createCell(appointment.getStatus(), NORMAL_FONT));

            // Добавляем отзыв, если он есть
            List<Review> reviews = reviewRepository.findByAppointmentId(appointmentId);
            log.info("Found {} reviews for appointment {}", reviews.size(), appointmentId);
            if (!reviews.isEmpty()) {
                table.addCell(createCell("Отзыв:", TITLE_FONT));
                String reviewTexts = reviews.stream()
                        .map(review -> {
                            String text = review.getReviewText();
                            log.info("Review text: {}", text);
                            return text;
                        })
                        .collect(Collectors.joining("\n"));
                table.addCell(createCell(reviewTexts, NORMAL_FONT));
                log.info("Added review text to PDF: {}", reviewTexts);
            } else {
                log.info("No reviews found for appointment {}", appointmentId);
            }

            document.add(table);

            // Подписи
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            Paragraph signatures = new Paragraph(
                    "Подпись клиента: _________________     Подпись мастера: _________________",
                    NORMAL_FONT
            );
            signatures.setAlignment(Element.ALIGN_CENTER);
            document.add(signatures);

        } catch (DocumentException e) {
            log.error("Error generating PDF", e);
        } finally {
            document.close();
        }
        return baos.toByteArray();
    }
}