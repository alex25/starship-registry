package com.w2m.starshipregistry.core.dto;

import java.io.Serializable;

import com.w2m.starshipregistry.core.dto.factories.MovieDtoFactory;

public record MovieDto(Long id, String title, Integer releaseYear, Boolean isTvSeries)
        implements Serializable, MovieDtoNullable {

    public static MovieDtoNullable fromMovieParent(MovieDtoNullable movieParent) {
        return MovieDtoFactory.create(
                movieParent.id(),
                movieParent.title(),
                movieParent.releaseYear(),
                movieParent.isTvSeries());
    }

    @Override
    public Long id() {
        return id;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public Integer releaseYear() {
        return releaseYear;
    }

    @Override
    public Boolean isTvSeries() {
        return isTvSeries;
    }

}
