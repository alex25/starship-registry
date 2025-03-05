package com.w2m.starshipregistry.infrastructure.adapters.inbound.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.w2m.starshipregistry.core.dtos.StarshipAddRequest;
import com.w2m.starshipregistry.core.dtos.StarshipUpdateRequest;
import com.w2m.starshipregistry.core.ports.inbound.AddStarshipPort;
import com.w2m.starshipregistry.core.ports.inbound.ModifyStarshipPort;
import com.w2m.starshipregistry.infrastructure.adapters.inbound.dtos.StarshipDataModificationRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReceiveDataModificationRabbit {

    private final ModifyStarshipPort modifyStarshipPort;

    @RabbitListener(queues = "#{@queue.inbound.name}") // Referencia a la cola de entrada
    public void receiveMessage(StarshipDataModificationRequest message) {
        System.out.println("Mensaje recibido: %s".formatted(message));
         StarshipUpdateRequest updateRequest = new StarshipUpdateRequest(message.name(), message.movieId());
        modifyStarshipPort.execute(message.id(), updateRequest);
        log.info("New Starship added from mq: {}", message);
    }
}