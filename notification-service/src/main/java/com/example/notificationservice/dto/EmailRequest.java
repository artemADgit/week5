package com.example.notificationservice.dto;

/**
 * DTO класс для запроса на отправку email через REST API.
 * 
 * Используется в контроллере для приема запросов на отправку уведомлений.
 */
public class EmailRequest {
    
    private String email;
    private String subject;
    private String message;
    
    // Конструктор по умолчанию
    public EmailRequest() {}
    
    public EmailRequest(String email, String subject, String message) {
        this.email = email;
        this.subject = subject;
        this.message = message;
    }
    
    // Геттеры и сеттеры
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
