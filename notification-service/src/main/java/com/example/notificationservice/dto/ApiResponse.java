package com.example.notificationservice.dto;

/**
 * DTO класс для ответов от REST API.
 * 
 * Содержит статус операции и сообщение для клиента.
 */
public class ApiResponse {
    
    private boolean success;
    private String message;
    
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    // Геттеры и сеттеры
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    // Статические методы для удобного создания ответов
    public static ApiResponse success(String message) {
        return new ApiResponse(true, message);
    }
    
    public static ApiResponse error(String message) {
        return new ApiResponse(false, message);
    }
}
