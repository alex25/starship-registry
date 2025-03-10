package com.w2m.starshipregistry.core.dto.factories;

import com.w2m.starshipregistry.core.dto.MovieDtoNullable;
import com.w2m.starshipregistry.core.dto.NullStarshipDto;
import com.w2m.starshipregistry.core.dto.StarshipDto;
import com.w2m.starshipregistry.core.dto.StarshipDtoNullable;

public final class StarshipDtoFactory {
    private static final NullStarshipDto NULL_INSTANCE = new NullStarshipDto();
    private StarshipDtoFactory() {}

    public static StarshipDtoNullable create(Long id, String name, MovieDtoNullable movie) {
        return new StarshipDto(id, name, movie);
    }

    public static StarshipDtoNullable nullInstance() {
        return NULL_INSTANCE;
    }

}