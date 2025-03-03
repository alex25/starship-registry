package com.w2m.starshipregistry.infrastructure.dto;

import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.entities.MovieEntity;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.entities.StarshipEntity;

public record StarshipDto(Long id, String name, MovieDto movieDto) {

    public StarshipDto(StarshipEntity starship) {
        this(
            starship.getId(),
            starship.getName(),
            starship.getMovie() != null ? new MovieDto(starship.getMovie()) : null
        );
    }

    public StarshipEntity toEntity() {
        return StarshipEntity.builder()
            .id(id)
            .name(name)
            .movie(movieDto.toEntity()) 
            .build();
    }

    public StarshipDto withMovie(MovieEntity newMovie) {
        return new StarshipDto(this.id(), this.name(), new MovieDto(newMovie));
    }
}
