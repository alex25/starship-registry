package com.w2m.starshipregistry.core.dtos;

import java.io.Serializable;

public record StarshipUpdateRequest(String name, Long movieId) implements Serializable {
}