package com.w2m.starshipregistry.infrastructure.mappers;

import com.w2m.starshipregistry.core.dtos.MovieDto;
import com.w2m.starshipregistry.core.dtos.MovieDtoNullable;
import com.w2m.starshipregistry.core.dtos.StarshipAddRequest;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.entities.MovieEntity;

public class MovieMapper {

    public static MovieDtoNullable toDto(MovieEntity entity) {
        return new MovieDto(
                entity.getId(),
                entity.getTitle(),
                entity.getReleaseYear(),
                entity.getIsTvSeries()
        );
    }

    public static MovieEntity toEntity(MovieDtoNullable dto) {
        return new MovieEntity(
                dto.id(),
                dto.title(),
                dto.releaseYear(),
                dto.isTvSeries()
        );
    }

    public static MovieEntity toEntity(StarshipAddRequest starshipAddRequest) {
        return new MovieEntity(
            starshipAddRequest.movieId(),
            starshipAddRequest.movieTitle(),
            starshipAddRequest.movieRelease(),
            starshipAddRequest.isTvSeries()
        );
    }
}