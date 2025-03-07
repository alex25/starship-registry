package com.w2m.starshipregistry.infrastructure.mappers;

import com.w2m.starshipregistry.core.dtos.NullMovieDto;
import com.w2m.starshipregistry.core.dtos.StarshipAddRequest;
import com.w2m.starshipregistry.core.dtos.StarshipDto;
import com.w2m.starshipregistry.core.dtos.StarshipDtoNullable;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.entities.StarshipEntity;

public class StarshipMapper {

    public static StarshipDtoNullable toDto(StarshipEntity entity) {
        return new StarshipDto(
                entity.getId(),
                entity.getName(),
                (entity.getMovie() == null ? new NullMovieDto() : MovieMapper.toDto(entity.getMovie())));
    }

    public static StarshipEntity toEntity(StarshipDtoNullable dto) {
        return new StarshipEntity(
                dto.id(),
                dto.name(),
                MovieMapper.toEntity(dto.movieDto()));
    }

    public static StarshipEntity toEntity(StarshipAddRequest starshipAddRequest) {
        return StarshipEntity.builder().name(starshipAddRequest.name()).build();
    }
}