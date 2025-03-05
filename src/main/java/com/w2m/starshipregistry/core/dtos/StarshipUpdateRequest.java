package com.w2m.starshipregistry.core.dtos;

import jakarta.validation.constraints.NotBlank;

public record StarshipUpdateRequest(String name, Long movieId) {
}