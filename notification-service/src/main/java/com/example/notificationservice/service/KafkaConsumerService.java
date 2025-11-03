package com.example.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.example.notificationservice.dto.UserEvent;

/**
 * Сервис для обработки сообщений из Kafka.
 * 
 * Слушает топик user-events и обрабатывает события создания/удаления пользователей.
 * Автоматически запускается при старте приложения и работает в фоновом режиме.
 */
@Service
public class KafkaConsumerService {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    
    private final EmailService emailService;
    
    /**
     * Конструктор с внедрением зависимости EmailService.
     * 
     * @param emailService - сервис для отправки email
     */
    public KafkaConsumerService(EmailService emailService) {
        this.emailService = emailService;
    }
    
    /**
     * Обработчик сообщений из Kafka топика user-events.
     * 
     * Этот метод автоматически вызывается при поступлении нового сообщения в топик.
     * Аннотация @KafkaListener настраивает подписку на указанный топик.
     * 
     * @param userEvent - десериализованное сообщение из Kafka
     */
    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void handleUserEvent(UserEvent userEvent) {
        logger.info("Получено событие из Kafka: {}", userEvent);
        
        try {
            // Определение типа операции и отправка соответствующего уведомления
            boolean sendResult;
            switch (userEvent.getOperation().toUpperCase()) {
                case "CREATE":
                    sendResult = emailService.sendAccountCreatedEmail(userEvent.getEmail());
                    break;
                case "DELETE":
                    sendResult = emailService.sendAccountDeletedEmail(userEvent.getEmail());
                    break;
                default:
                    logger.warn("Неизвестная операция: {}", userEvent.getOperation());
                    return;
            }
            
            if (sendResult) {
                logger.info("Уведомление для операции '{}' успешно отправлено на {}", 
                           userEvent.getOperation(), userEvent.getEmail());
            } else {
                logger.error("Не удалось отправить уведомление для операции '{}' на {}", 
                            userEvent.getOperation(), userEvent.getEmail());
            }
            
        } catch (Exception e) {
            logger.error("Ошибка при обработке события пользователя: {}", userEvent, e);
        }
    }
}
