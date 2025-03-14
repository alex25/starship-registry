package com.w2m.starshipregistry.core.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.w2m.starshipregistry.core.dto.factories.MovieDtoFactory;
import com.w2m.starshipregistry.core.dto.factories.StarshipDtoFactory;

public record StarshipDto(Long id, String name, @JsonProperty("movie") MovieDtoNullable movieDto)
        implements Serializable, StarshipDtoNullable {

    public StarshipDtoNullable withMovie(MovieDtoNullable newMovie) {
        return StarshipDtoFactory.create(this.id(), this.name(), newMovie);
    }

    public StarshipDtoNullable updateMovieId(Long movieId) {
        return StarshipDtoFactory.create(this.id(), this.name(),
                MovieDtoFactory.create(movieId, this.movieDto.title(), this.movieDto.releaseYear(),
                        this.movieDto.isTvSeries()));
    }

    public StarshipDtoNullable updateName(String name2) {
        return StarshipDtoFactory.create(this.id(), name2, this.movieDto);
    }

}
