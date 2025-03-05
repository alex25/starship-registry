package com.w2m.starshipregistry.core.dtos;

import java.io.Serializable;

public record MovieDto(Long id, String title, Integer releaseYear, Boolean isTvSeries)
        implements Serializable, MovieDtoNullable {

    public static MovieDto fromMovieParent(MovieDtoNullable movieParent) {
        return new MovieDto(
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
