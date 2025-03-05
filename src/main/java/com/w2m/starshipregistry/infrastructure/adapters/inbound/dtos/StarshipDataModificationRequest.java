package com.w2m.starshipregistry.infrastructure.adapters.inbound.dtos;

public record StarshipDataModificationRequest(Long id, String name, Long movieId) {
}