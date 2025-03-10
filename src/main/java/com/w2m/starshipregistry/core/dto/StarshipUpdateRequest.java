package com.w2m.starshipregistry.core.dto;

import java.io.Serializable;

public record StarshipUpdateRequest(String name, Long movieId) implements Serializable {
}