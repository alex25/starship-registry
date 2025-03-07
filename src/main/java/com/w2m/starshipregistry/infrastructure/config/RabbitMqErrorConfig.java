package com.w2m.starshipregistry.infrastructure.config;

import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RabbitMqErrorConfig {

    @Bean
    public RabbitListenerErrorHandler rabbitErrorHandler() {
        return (message, channel, amqpMessage, exception) -> {
            log.error("Message fail: %s - %s".formatted(new String(message.getBody()), exception.getMessage()));
            exception.printStackTrace();
            
            throw exception;

        };
    }
}