package com.w2m.starshipregistry.infrastructure.mappers;

import com.w2m.starshipregistry.core.dto.StarshipAddRequest;
import com.w2m.starshipregistry.core.dto.StarshipDtoNullable;
import com.w2m.starshipregistry.core.dto.factories.MovieDtoFactory;
import com.w2m.starshipregistry.core.dto.factories.StarshipDtoFactory;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.entities.MovieEntity;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.entities.StarshipEntity;

public class StarshipMapper {

    public static StarshipDtoNullable toDto(StarshipEntity entity) {
        return StarshipDtoFactory.create(
                entity.getId(),
                entity.getName(),
                entity.getMovie() == null 
                    ? MovieDtoFactory.nullInstance()
                    : MovieMapper.toDto(entity.getMovie())
        );

    }

    public static StarshipEntity toEntity(StarshipDtoNullable dto) {
        return new StarshipEntity(
                dto.id(),
                dto.name(),
                MovieMapper.toEntity(dto.movieDto()));
    }

    public static StarshipEntity toEntity(StarshipAddRequest request) {
        return StarshipEntity.builder()
            .name(request.name())
            .movie(MovieEntity.builder().id(request.movieId()).build())
            .build();
    }

}
