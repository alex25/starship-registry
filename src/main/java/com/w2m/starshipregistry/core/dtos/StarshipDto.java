package com.w2m.starshipregistry.core.dtos;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StarshipDto(Long id, String name, @JsonProperty("movie") MovieDtoNullable movieDto)
        implements Serializable, StarshipDtoNullable {

    public StarshipDtoNullable withMovie(MovieDtoNullable newMovie) {
        return new StarshipDto(this.id(), this.name(), newMovie);
    }

    public StarshipDtoNullable updateMovieId(Long movieId) {
        return new StarshipDto(this.id(), this.name(),
                new MovieDto(movieId, this.movieDto.title(), this.movieDto.releaseYear(),
                        this.movieDto.isTvSeries()));
    }

    public StarshipDtoNullable updateName(String name2) {
        return new StarshipDto(this.id(), name2, this.movieDto);
    }

}
