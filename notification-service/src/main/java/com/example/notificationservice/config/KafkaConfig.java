package com.example.notificationservice.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import com.example.notificationservice.dto.UserEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурационный класс для настройки Kafka Consumer.
 * 
 * Определяет параметры подключения к Kafka и настройки десериализации сообщений.
 */
@Configuration
public class KafkaConfig {
    
    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;
    
    @Value("${spring.kafka.consumer.group-id:notification-group}")
    private String groupId;
    
    /**
     * Создает фабрику потребителей Kafka для десериализации сообщений.
     * 
     * @return ConsumerFactory настроенный на работу с UserEvent объектами
     */
    @Bean
    public ConsumerFactory<String, UserEvent> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        
        // Основные настройки подключения к Kafka
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        
        // Настройки десериализации ключа (String) и значения (UserEvent)
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        
        // Дополнительные настройки для JsonDeserializer
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.example.notificationservice.dto");
        props.put(JsonDeserializer.TYPE_MAPPINGS, "userEvent:com.example.notificationservice.dto.UserEvent");
        
        return new DefaultKafkaConsumerFactory<>(
            props,
            new StringDeserializer(),
            new JsonDeserializer<>(UserEvent.class)
        );
    }
    
    /**
     * Создает фабрику для Kafka Listener контейнеров.
     * 
     * @return ConcurrentKafkaListenerContainerFactory для обработки сообщений
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserEvent> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
