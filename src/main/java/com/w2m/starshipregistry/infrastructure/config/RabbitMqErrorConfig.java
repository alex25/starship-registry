package com.w2m.starshipregistry.infrastructure.config;

import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RabbitMqErrorConfig {

    @Bean
    public RabbitListenerErrorHandler rabbitErrorHandler() {
        return (message, channel, amqpMessage, exception) -> {
            log.error("Message fail: %s - %s".formatted(new String(message.getBody()), exception.getMessage()));
            exception.printStackTrace();

            // reject and send to DLQ
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);            
            
            throw exception;

        };
    }

    @Bean
    public RejectAndDontRequeueRecoverer recoverer() {
        return new RejectAndDontRequeueRecoverer();
    }

    @Bean
    public RetryOperationsInterceptor retryInterceptor(RejectAndDontRequeueRecoverer recoverer)  {
        return RetryInterceptorBuilder.stateless()
            .maxAttempts(3)
            .backOffOptions(1000, 2.0, 10000)
            .recoverer(recoverer)
            .build();
    }
}