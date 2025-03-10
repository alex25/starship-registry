package com.w2m.starshipregistry.core.dto;

public record NullStarshipDto() implements StarshipDtoNullable {
    @Override
    public Long id() {
        return null;
    }

    @Override
    public String name() {
        return "Not Found";
    }

    @Override
    public MovieDtoNullable movieDto() {
        return null;
    }

    @Override
    public boolean isNull() {
        return true;
    }

    public StarshipDtoNullable withMovie(MovieDtoNullable newMovie) {
        return null;
    }

    @Override
    public StarshipDtoNullable updateName(String name) {
        return null;
    }

    @Override
    public StarshipDtoNullable updateMovieId(Long movieId) {
        return null;
    }

}