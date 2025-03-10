package com.w2m.starshipregistry.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = RabbitMqConfig.class)
@TestPropertySource(properties = {
    "spring.rabbitmq.queue.inbound.name=inboundQueue",
    "spring.rabbitmq.queue.outbound.name=outboundQueue",
    "spring.rabbitmq.queue.outbound.durable=true",
    "spring.rabbitmq.template.exchange=mainExchange",
    "spring.rabbitmq.template.routing-key.outbound=outboundKey"
})
class RabbitMqConfigTest {

    @Autowired
    private Queue inboundQueue;
    
    @Autowired
    private Queue outboundQueue;
    
    @Autowired
    @Qualifier("exchange")
    private DirectExchange mainExchange;
    
    @Autowired
    @Qualifier("deadLetterExchange")
    private DirectExchange deadLetterExchange;
    
    @Autowired
    private Queue deadLetterQueue;
    
    @Autowired
    private Binding outboundBinding;
    
    @Autowired
    private Binding dlqBinding;
    
    @Autowired
    private MessageConverter messageConverter;

    @Test
    void inboundQueueConfiguration() {
        assertThat(inboundQueue.getName()).isEqualTo("inboundQueue");
        assertThat(inboundQueue.isDurable()).isTrue();
        assertThat(inboundQueue.getArguments())
            .containsEntry("x-dead-letter-exchange", "dlq.exchange")
            .containsEntry("x-dead-letter-routing-key", "dlq.key");
    }

    @Test
    void outboundQueueConfiguration() {
        assertThat(outboundQueue.getName()).isEqualTo("outboundQueue");
        assertThat(outboundQueue.isDurable()).isTrue();
        assertThat(outboundQueue.getArguments())
            .containsEntry("x-dead-letter-exchange", "dlq.exchange")
            .containsEntry("x-dead-letter-routing-key", "dlq.key");
    }

    @Test
    void exchanges() {
        assertThat(mainExchange.getName()).isEqualTo("mainExchange");
        assertThat(mainExchange.isDurable()).isTrue();
        
        assertThat(deadLetterExchange.getName()).isEqualTo("dlq.exchange");
        assertThat(deadLetterExchange.isDurable()).isTrue();
    }

    @Test
    void bindings() {
        assertThat(outboundBinding.getExchange()).isEqualTo("mainExchange");
        assertThat(outboundBinding.getRoutingKey()).isEqualTo("outboundKey");
        
        assertThat(dlqBinding.getExchange()).isEqualTo("dlq.exchange");
        assertThat(dlqBinding.getRoutingKey()).isEqualTo("dlq.key");
    }

    @Test
    void messageConverter() {
        assertThat(messageConverter).isInstanceOf(Jackson2JsonMessageConverter.class);
    }

    @Test
    void deadLetterQueue() {
        assertThat(deadLetterQueue.getName()).isEqualTo("dlq.queue");
        assertThat(deadLetterQueue.isDurable()).isTrue();
    }
}