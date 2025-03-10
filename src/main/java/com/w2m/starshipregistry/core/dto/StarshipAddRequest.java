package com.w2m.starshipregistry.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StarshipAddRequest(@NotNull() @NotBlank() String name, 
@NotNull() Long movieId, String movieTitle, Integer movieRelease, 
Boolean isTvSeries) {
    
}
