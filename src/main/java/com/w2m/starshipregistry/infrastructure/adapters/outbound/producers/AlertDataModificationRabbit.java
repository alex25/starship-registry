package com.w2m.starshipregistry.infrastructure.adapters.outbound.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.w2m.starshipregistry.core.ports.outbound.AlertDataModificationPort;
import com.w2m.starshipregistry.infrastructure.adapters.inbound.dtos.StarshipDataModificationRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertDataModificationRabbit implements AlertDataModificationPort {

    private final RabbitTemplate rabbitTemplate;

    public void notifyStarshipModification(Long id, String name, Long movieId) {
        StarshipDataModificationRequest message = new StarshipDataModificationRequest(id, name, movieId);

        rabbitTemplate.convertAndSend("starship-exchange", "starship.modification", message);
        log.info("Message %s send to starship.modification", message);
    }
}