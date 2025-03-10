package com.w2m.starshipregistry.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.rabbitmq.client.Channel;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RabbitMqErrorConfig.class)
public class RabbitMqErrorConfigTest {

    @Mock
    private Channel channel;

    @Mock
    private Message message; // Cambiado a Message<?>

    @Mock
    private MessageProperties messageProperties;

    @InjectMocks
    private RabbitMqErrorConfig rabbitMqErrorConfig;

    @Autowired
    private RejectAndDontRequeueRecoverer recoverer;

    @Test
    public void rabbitErrorHandlerShouldLogErrorAndRejectMessage() throws Exception {
        // Arrange
        RabbitListenerErrorHandler errorHandler = rabbitMqErrorConfig.rabbitErrorHandler();
        ListenerExecutionFailedException exception = new ListenerExecutionFailedException("Test error", new RuntimeException("Test exception"));

        when(message.getBody()).thenReturn("Test message".getBytes());
        when(message.getMessageProperties()).thenReturn(messageProperties);
        when(messageProperties.getDeliveryTag()).thenReturn(1L);

        // Act
        assertThrows(ListenerExecutionFailedException.class, () -> {
            errorHandler.handleError(message, channel, null, exception); // Ahora es compatible
        });

        // Assert
        verify(channel, times(1)).basicNack(1L, false, false);
    }

    @Test
    public void recovererShouldBeConfiguredCorrectly() {
        assertThat(recoverer).isNotNull();
    }
    
}