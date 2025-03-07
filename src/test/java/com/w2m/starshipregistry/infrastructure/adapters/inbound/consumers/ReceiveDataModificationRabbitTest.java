package com.w2m.starshipregistry.infrastructure.adapters.inbound.consumers;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.w2m.starshipregistry.core.dtos.StarshipUpdateRequest;
import com.w2m.starshipregistry.core.ports.inbound.ModifyStarshipFromMqPort;
import com.w2m.starshipregistry.infrastructure.adapters.inbound.dtos.StarshipDataModificationRequest;

@ExtendWith(MockitoExtension.class)
public class ReceiveDataModificationRabbitTest {

    @Mock
    private ModifyStarshipFromMqPort modifyStarshipFromMqPort;

    @InjectMocks
    private ReceiveDataModificationRabbit consumer;

    @Test
    void testReceiveMessage() {
        // Arrange
        StarshipDataModificationRequest message = 
            new StarshipDataModificationRequest(1L, "Enterprise XI", 101L);

        // Act
        consumer.receiveMessage(message);

        // Assert
        verify(modifyStarshipFromMqPort).execute(
            eq(1L),
            eq(new StarshipUpdateRequest("Enterprise XI", 101L))
        );
    }
}
