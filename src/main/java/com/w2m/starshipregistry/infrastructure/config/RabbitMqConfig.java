package com.w2m.starshipregistry.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("${spring.rabbitmq.queue.inbound.name}")
    private String inboundQueueName;

    @Value("${spring.rabbitmq.queue.outbound.name}")
    private String outboundQueueName;

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.template.routing-key.outbound}")
    private String outboundRoutingKey;

    @Bean
    public Queue inboundQueue() {
        return QueueBuilder.durable(inboundQueueName)
                .withArgument("x-dead-letter-exchange", "dlq.exchange") // DLQ Exchange
                .withArgument("x-dead-letter-routing-key", "dlq.key")   // Routing Key para DLQ
                .build();
    }

    @Bean
    public Queue outboundQueue() {
        return QueueBuilder.durable(outboundQueueName)
                .withArgument("x-dead-letter-exchange", "dlq.exchange")
                .withArgument("x-dead-letter-routing-key", "dlq.key")
                .build();
    }
    
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding outboundBinding(Queue outboundQueue, DirectExchange exchange) {
        return BindingBuilder.bind(outboundQueue)
                .to(exchange)
                .with(outboundRoutingKey);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange("dlq.exchange");
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable("dlq.queue").build();
    }

    @Bean
    public Binding dlqBinding(DirectExchange deadLetterExchange, Queue deadLetterQueue) {
        return BindingBuilder.bind(deadLetterQueue)
                .to(deadLetterExchange)
                .with("dlq.key");
    }
}
