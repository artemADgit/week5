package com.example.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Сервис для отправки email-уведомлений.
 * 
 * Инкапсулирует логику работы с почтовым сервером и отправки сообщений.
 * Использует Spring Mail для интеграции с SMTP-сервером.
 */
@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    private final JavaMailSender mailSender;
    
    // Тема письма для создания аккаунта
    private static final String CREATE_SUBJECT = "Добро пожаловать!";
    // Тема письма для удаления аккаунта
    private static final String DELETE_SUBJECT = "Ваш аккаунт удален";
    
    /**
     * Конструктор с внедрением зависимости JavaMailSender.
     * 
     * @param mailSender - компонент Spring для отправки email
     */
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    /**
     * Отправляет уведомление о создании аккаунта.
     * 
     * @param email - адрес получателя
     * @return true если сообщение отправлено успешно, false в случае ошибки
     */
    public boolean sendAccountCreatedEmail(String email) {
        String message = "Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.";
        return sendEmail(email, CREATE_SUBJECT, message);
    }
    
    /**
     * Отправляет уведомление об удалении аккаунта.
     * 
     * @param email - адрес получателя
     * @return true если сообщение отправлено успешно, false в случае ошибки
     */
    public boolean sendAccountDeletedEmail(String email) {
        String message = "Здравствуйте! Ваш аккаунт был удалён.";
        return sendEmail(email, DELETE_SUBJECT, message);
    }
    
    /**
     * Отправляет произвольное email-сообщение.
     * 
     * @param email - адрес получателя
     * @param subject - тема письма
     * @param message - текст сообщения
     * @return true если сообщение отправлено успешно, false в случае ошибки
     */
    public boolean sendEmail(String email, String subject, String message) {
        try {
            logger.info("Попытка отправки email на адрес: {}, тема: {}", email, subject);
            
            // Создание объекта email сообщения
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            
            // Отправка сообщения
            mailSender.send(mailMessage);
            
            logger.info("Email успешно отправлен на адрес: {}", email);
            return true;
            
        } catch (Exception e) {
            logger.error("Ошибка при отправке email на адрес: {}", email, e);
            return false;
        }
    }
}
