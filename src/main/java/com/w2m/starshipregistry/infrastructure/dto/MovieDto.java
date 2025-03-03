package com.w2m.starshipregistry.infrastructure.dto;

import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.entities.MovieEntity;

public record MovieDto(Long id, String title, Integer releaseYear, Boolean isTvSeries) {

    public MovieDto(MovieEntity movieEntity) {
        this(
            movieEntity.getId(),
            movieEntity.getTitle(),
            movieEntity.getReleaseYear(),
            movieEntity.getIsTvSeries()
        );
    }

    public MovieEntity toEntity() {
        return MovieEntity.builder()
            .id(id)
            .title(title)
            .releaseYear(releaseYear)
            .isTvSeries(isTvSeries)
            .build();
    }
}
