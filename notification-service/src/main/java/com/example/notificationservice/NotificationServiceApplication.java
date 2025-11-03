package com.example.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Основной класс приложения Notification Service.
 * 
 * Этот микросервис отвечает за:
 * 1. Обработку сообщений из Kafka о событиях пользователей
 * 2. Отправку email-уведомлений при создании/удалении пользователей
 * 3. Предоставление REST API для ручной отправки уведомлений
 */
@SpringBootApplication
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}
