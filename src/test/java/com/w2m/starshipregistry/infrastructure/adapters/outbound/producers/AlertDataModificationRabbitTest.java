package com.w2m.starshipregistry.infrastructure.adapters.outbound.producers;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.w2m.starshipregistry.infrastructure.adapters.inbound.dtos.StarshipDataModificationRequest;

@ExtendWith(MockitoExtension.class)
public class AlertDataModificationRabbitTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    private AlertDataModificationRabbit alertDataModificationRabbit;

    @BeforeEach
    void setUp() {
        alertDataModificationRabbit = new AlertDataModificationRabbit(rabbitTemplate);
    }

    @Test
    void notifyStarshipModificationShouldSendCorrectMessage() {
        // Arrange
        Long id = 1L;
        String name = "Millennium Falcon";
        Long movieId = 5L;

        // Act
        alertDataModificationRabbit.notifyStarshipModification(id, name, movieId);

        // Assert
        ArgumentCaptor<StarshipDataModificationRequest> messageCaptor = ArgumentCaptor.forClass(StarshipDataModificationRequest.class);
        verify(rabbitTemplate).convertAndSend(eq("starship-exchange"), eq("starship.modification"), messageCaptor.capture());
        
        StarshipDataModificationRequest capturedMessage = messageCaptor.getValue();
        assertThat(capturedMessage.id()).isEqualTo(id);
        assertThat(capturedMessage.name()).isEqualTo(name);
        assertThat(capturedMessage.movieId()).isEqualTo(movieId);
    }
}
