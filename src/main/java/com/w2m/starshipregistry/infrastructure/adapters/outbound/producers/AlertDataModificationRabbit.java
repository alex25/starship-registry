package com.w2m.starshipregistry.infrastructure.adapters.outbound.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.w2m.starshipregistry.infrastructure.adapters.inbound.dtos.StarshipDataModificationRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlertDataModificationRabbit {

    private final RabbitTemplate rabbitTemplate;

    public void notifyStarshipModification(Long id, String name, Long movieId) {
        // Crear el mensaje
        StarshipDataModificationRequest message = new StarshipDataModificationRequest(id, name, movieId);

        // Enviar el mensaje a la cola de salida
        rabbitTemplate.convertAndSend("starship-exchange", "starship.modification", message);
        System.out.println("Mensaje enviado: " + message);
    }
}