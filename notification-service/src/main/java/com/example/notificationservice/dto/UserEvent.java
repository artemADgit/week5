package com.example.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO класс для представления события пользователя из Kafka.
 * 
 * Содержит информацию об операции (создание/удаление) и email пользователя.
 * Сообщения в этом формате отправляются user-service при изменениях пользователей.
 */
public class UserEvent {
    
    @JsonProperty("operation")
    private String operation; // CREATE или DELETE
    
    @JsonProperty("email")
    private String email;
    
    // Конструктор по умолчанию (необходим для Jackson)
    public UserEvent() {}
    
    public UserEvent(String operation, String email) {
        this.operation = operation;
        this.email = email;
    }
    
    // Геттеры и сеттеры
    public String getOperation() {
        return operation;
    }
    
    public void setOperation(String operation) {
        this.operation = operation;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public String toString() {
        return "UserEvent{operation='" + operation + "', email='" + email + "'}";
    }
}
