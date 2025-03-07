package com.w2m.starshipregistry.infrastructure.adapters.inbound.dtos;

import java.io.Serializable;

public record StarshipDataModificationRequest(Long id, String name, Long movieId) implements Serializable{
}