package com.example.notificationservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.example.notificationservice.dto.EmailRequest;
import com.example.notificationservice.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit тесты для NotificationController.
 * 
 * Проверяет корректность работы REST API контроллера.
 */
@WebMvcTest(NotificationController.class)
class NotificationControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private EmailService emailService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * Тест успешной отправки email через REST API.
     */
    @Test
    void testSendEmailSuccess() throws Exception {
        // Подготовка тестовых данных
        EmailRequest emailRequest = new EmailRequest("test@example.com", "Test Subject", "Test Message");
        
        // Настройка моков
        when(emailService.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);
        
        // Вызов и проверка REST endpoint
        mockMvc.perform(post("/api/notifications/send-email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").exists());
    }
    
    /**
     * Тест отправки email с ошибкой через REST API.
     */
    @Test
    void testSendEmailFailure() throws Exception {
        // Подготовка тестовых данных
        EmailRequest emailRequest = new EmailRequest("test@example.com", "Test Subject", "Test Message");
        
        // Настройка моков (симуляция ошибки)
        when(emailService.sendEmail(anyString(), anyString(), anyString())).thenReturn(false);
        
        // Вызов и проверка REST endpoint
        mockMvc.perform(post("/api/notifications/send-email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").exists());
    }
}
