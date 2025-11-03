package com.example.notificationservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.notificationservice.dto.ApiResponse;
import com.example.notificationservice.dto.EmailRequest;
import com.example.notificationservice.service.EmailService;

/**
 * REST контроллер для управления уведомлениями.
 * 
 * Предоставляет API для ручной отправки email-уведомлений.
 * Доступен по базовому пути /api/notifications.
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    
    private final EmailService emailService;
    
    /**
     * Конструктор с внедрением зависимости EmailService.
     * 
     * @param emailService - сервис для отправки email
     */
    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }
    
    /**
     * REST endpoint для отправки произвольного email-сообщения.
     * 
     * Метод принимает POST запрос с телом в формате JSON, содержащим
     * адрес получателя, тему и текст сообщения.
     * 
     * @param emailRequest - DTO с данными для отправки email
     * @return ApiResponse с результатом операции
     */
    @PostMapping("/send-email")
    public ApiResponse sendEmail(@RequestBody EmailRequest emailRequest) {
        logger.info("Получен запрос на отправку email: {}", emailRequest);
        
        // Валидация входных данных
        if (emailRequest.getEmail() == null || emailRequest.getEmail().trim().isEmpty()) {
            return ApiResponse.error("Email адрес не может быть пустым");
        }
        
        if (emailRequest.getMessage() == null || emailRequest.getMessage().trim().isEmpty()) {
            return ApiResponse.error("Текст сообщения не может быть пустым");
        }
        
        // Отправка email
        boolean sendResult = emailService.sendEmail(
            emailRequest.getEmail(),
            emailRequest.getSubject() != null ? emailRequest.getSubject() : "Уведомление",
            emailRequest.getMessage()
        );
        
        // Формирование ответа
        if (sendResult) {
            return ApiResponse.success("Email успешно отправлен на адрес: " + emailRequest.getEmail());
        } else {
            return ApiResponse.error("Не удалось отправить email на адрес: " + emailRequest.getEmail());
        }
    }
}
