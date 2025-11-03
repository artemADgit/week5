package com.example.notificationservice.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import com.example.notificationservice.dto.UserEvent;
import com.example.notificationservice.service.EmailService;
import com.example.notificationservice.service.KafkaConsumerService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

/**
 * Интеграционные тесты для проверки отправки сообщений на почту.
 * 
 * Использует EmbeddedKafka для тестирования Kafka функциональности
 * и MockBean для мокирования почтового сервиса.
 */
@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"user-events"})
@TestPropertySource(properties = {
    "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
    "spring.mail.host=localhost",
    "spring.mail.port=3025"
})
class NotificationServiceIntegrationTest {
    
    @Autowired
    private KafkaTemplate<String, UserEvent> kafkaTemplate;
    
    @Autowired
    private EmailService emailService;
    
    @MockBean
    private JavaMailSender mailSender;
    
    @Autowired
    private KafkaConsumerService kafkaConsumerService;
    
    /**
     * Тест обработки события создания пользователя через Kafka.
     * 
     * Проверяет, что при получении сообщения CREATE из Kafka
     * отправляется корректное email-уведомление.
     * 
     * @throws Exception - в случае ошибок тестирования
     */
    @Test
    void testUserCreatedEvent() throws Exception {
        // Создание тестового события
        UserEvent userEvent = new UserEvent("CREATE", "test@example.com");
        
        // Отправка события в Kafka
        kafkaTemplate.send("user-events", userEvent);
        
        // Ожидание и проверка, что email был отправлен
        verify(mailSender, timeout(5000)).send(any(SimpleMailMessage.class));
    }
    
    /**
     * Тест обработки события удаления пользователя через Kafka.
     * 
     * Проверяет, что при получении сообщения DELETE из Kafka
     * отправляется корректное email-уведомление об удалении.
     * 
     * @throws Exception - в случае ошибок тестирования
     */
    @Test
    void testUserDeletedEvent() throws Exception {
        // Создание тестового события
        UserEvent userEvent = new UserEvent("DELETE", "test@example.com");
        
        // Отправка события в Kafka
        kafkaTemplate.send("user-events", userEvent);
        
        // Ожидание и проверка, что email был отправлен
        verify(mailSender, timeout(5000)).send(any(SimpleMailMessage.class));
    }
    
    /**
     * Тест прямого вызова сервиса отправки email.
     * 
     * Проверяет корректность работы EmailService без участия Kafka.
     */
    @Test
    void testEmailServiceDirectly() {
        // Вызов сервиса отправки email
        emailService.sendAccountCreatedEmail("test@example.com");
        
        // Проверка, что email был отправлен
        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}
