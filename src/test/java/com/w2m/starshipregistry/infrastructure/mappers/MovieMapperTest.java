package com.w2m.starshipregistry.infrastructure.mappers;

import org.junit.jupiter.api.Test;

import com.w2m.starshipregistry.core.dto.MovieDtoNullable;
import com.w2m.starshipregistry.core.dto.StarshipAddRequest;
import com.w2m.starshipregistry.core.dto.factories.MovieDtoFactory;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.entities.MovieEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class MovieMapperTest {

    @Test
    public void toDtoShouldMapEntityToDtoCorrectly() {
        // Arrange
        MovieEntity entity = new MovieEntity(1L, "Inception", 2010, false);

        // Act
        MovieDtoNullable dto = MovieMapper.toDto(entity);

        // Assert
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.title()).isEqualTo("Inception");
        assertThat(dto.releaseYear()).isEqualTo(2010);
        assertThat(dto.isTvSeries()).isFalse();
    }

    @Test
    public void toEntityShouldMapDtoToEntityCorrectly() {
        // Arrange
        MovieDtoNullable dto = MovieDtoFactory.create(1L, "Inception", 2010, false);

        // Act
        MovieEntity entity = MovieMapper.toEntity(dto);

        // Assert
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getTitle()).isEqualTo("Inception");
        assertThat(entity.getReleaseYear()).isEqualTo(2010);
        assertThat(entity.getIsTvSeries()).isFalse();
    }

    @Test
    public void toEntityShouldMapStarshipAddRequestToEntityCorrectly() {
        // Arrange
        StarshipAddRequest request = new StarshipAddRequest("Strangership", 11L, "Inception", 2010, false);

        // Act
        MovieEntity entity = MovieMapper.toEntity(request);

        // Assert
        assertThat(entity.getId()).isEqualTo(11L);
        assertThat(entity.getTitle()).isEqualTo("Inception");
        assertThat(entity.getReleaseYear()).isEqualTo(2010);
        assertThat(entity.getIsTvSeries()).isFalse();
    }
}
