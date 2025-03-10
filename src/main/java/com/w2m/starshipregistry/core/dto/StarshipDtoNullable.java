package com.w2m.starshipregistry.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;


public interface StarshipDtoNullable {

    Long id();
    String name();
    MovieDtoNullable movieDto();
    
    @JsonIgnore
    default boolean isNull() {
        return false;
    }
    
    StarshipDtoNullable updateName(String name);
    StarshipDtoNullable updateMovieId(Long movieId);
    StarshipDtoNullable withMovie(MovieDtoNullable newMovie);

}