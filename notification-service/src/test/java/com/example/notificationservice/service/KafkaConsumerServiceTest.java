package com.example.notificationservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.notificationservice.dto.UserEvent;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit тесты для KafkaConsumerService.
 * 
 * Проверяет бизнес-логику обработки сообщений из Kafka.
 */
@ExtendWith(MockitoExtension.class)
class KafkaConsumerServiceTest {
    
    @Mock
    private EmailService emailService;
    
    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;
    
    /**
     * Тест обработки события создания пользователя.
     */
    @Test
    void testHandleCreateUserEvent() {
        // Подготовка тестовых данных
        UserEvent userEvent = new UserEvent("CREATE", "test@example.com");
        
        // Настройка моков
        when(emailService.sendAccountCreatedEmail("test@example.com")).thenReturn(true);
        
        // Вызов тестируемого метода
        kafkaConsumerService.handleUserEvent(userEvent);
        
        // Проверка вызовов
        verify(emailService, times(1)).sendAccountCreatedEmail("test@example.com");
    }
    
    /**
     * Тест обработки события удаления пользователя.
     */
    @Test
    void testHandleDeleteUserEvent() {
        // Подготовка тестовых данных
        UserEvent userEvent = new UserEvent("DELETE", "test@example.com");
        
        // Настройка моков
        when(emailService.sendAccountDeletedEmail("test@example.com")).thenReturn(true);
        
        // Вызов тестируемого метода
        kafkaConsumerService.handleUserEvent(userEvent);
        
        // Проверка вызовов
        verify(emailService, times(1)).sendAccountDeletedEmail("test@example.com");
    }
}
