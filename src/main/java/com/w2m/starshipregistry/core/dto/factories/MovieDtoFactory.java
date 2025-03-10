package com.w2m.starshipregistry.core.dto.factories;

import com.w2m.starshipregistry.core.dto.MovieDto;
import com.w2m.starshipregistry.core.dto.MovieDtoNullable;
import com.w2m.starshipregistry.core.dto.NullMovieDto;

public final class MovieDtoFactory {
    private static final NullMovieDto NULL_INSTANCE = new NullMovieDto();
    private MovieDtoFactory() {}

    public static MovieDtoNullable create(Long id, String title, Integer releaseYear, Boolean isTvSeries) {
        return new MovieDto(id, title, releaseYear, isTvSeries);
    }

    public static MovieDtoNullable nullInstance() {
        return NULL_INSTANCE;
    }

}