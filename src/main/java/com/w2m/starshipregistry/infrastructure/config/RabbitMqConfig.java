package com.w2m.starshipregistry.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
	@Value("${spring.rabbitmq.queue.inbound.name}")
	private String inboundQueueName;

	@Bean
	public Queue inboundQueue() {
		return new Queue(inboundQueueName);
	}

	@Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
	    
    @Value("${spring.rabbitmq.queue.outbound.name}")
    private String outboundQueueName;
    
    @Value("${spring.rabbitmq.queue.outbound.durable}")
    private boolean outboundQueueDurable;
    
    @Value("${spring.rabbitmq.template.exchange}")
    private String exchange;
    
    @Value("${spring.rabbitmq.template.routing-key.outbound}")
    private String outboundRoutingKey;
    
    @Bean
    Queue outboundQueue() {
        return new Queue(outboundQueueName, outboundQueueDurable, false, false);
    }
    
    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchange);
    }
    
    @Bean
    Binding outboundBinding(Queue outboundQueue, DirectExchange exchange) {
        return BindingBuilder.bind(outboundQueue)
            .to(exchange)
            .with(outboundRoutingKey);
    }
}
