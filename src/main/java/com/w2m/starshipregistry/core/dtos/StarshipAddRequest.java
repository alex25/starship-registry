package com.w2m.starshipregistry.core.dtos;

import jakarta.validation.constraints.NotBlank;

public record StarshipAddRequest(@NotBlank() String name, 
Long movieId, String movieTitle, Integer movieRelease, 
Boolean isTvSeries) {
    
}
