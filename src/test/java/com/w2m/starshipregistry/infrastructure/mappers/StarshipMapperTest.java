package com.w2m.starshipregistry.infrastructure.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.w2m.starshipregistry.core.dto.MovieDto;
import com.w2m.starshipregistry.core.dto.MovieDtoNullable;
import com.w2m.starshipregistry.core.dto.StarshipAddRequest;
import com.w2m.starshipregistry.core.dto.StarshipDto;
import com.w2m.starshipregistry.core.dto.StarshipDtoNullable;
import com.w2m.starshipregistry.core.dto.factories.MovieDtoFactory;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.entities.MovieEntity;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.entities.StarshipEntity;

@ExtendWith(MockitoExtension.class)
class StarshipMapperTest {

    private static final Long ID = 1L;
    private static final String NAME = "Millennium Falcon";
    private static final Long MOVIE_ID = 4L;
    private static final String MOVIE_TITLE = "Star Wars";
    private static final int MOVIE_YEAR = 1977;
    private static final boolean MOVIE_OSCAR = false;

    @Test
    void toDtoWithMovieShouldMapCorrectly() {
        // Arrange
        MovieEntity movieEntity = new MovieEntity(MOVIE_ID, MOVIE_TITLE, MOVIE_YEAR, MOVIE_OSCAR);
        StarshipEntity entity = new StarshipEntity(ID, NAME, movieEntity);
        MovieDto movieDto = new MovieDto(MOVIE_ID, MOVIE_TITLE, MOVIE_YEAR, MOVIE_OSCAR);

        try (MockedStatic<MovieMapper> movieMapperMock = mockStatic(MovieMapper.class)) {
            movieMapperMock.when(() -> MovieMapper.toDto(movieEntity)).thenReturn(movieDto);

            // Act
            StarshipDtoNullable result = StarshipMapper.toDto(entity);

            // Assert
            assertThat(result.id()).isEqualTo(ID);
            assertThat(result.name()).isEqualTo(NAME);
            assertThat(result.movieDto()).isEqualTo(movieDto);
        }
    }

    @Test
    void toDtoWithNullMovieShouldUseNullMovieDto() {
        // Arrange
        StarshipEntity entity = new StarshipEntity(ID, NAME, null);
        MovieDtoNullable nullMovieDto = mock(MovieDtoNullable.class);

        try (MockedStatic<MovieDtoFactory> dtoFactoryMock = mockStatic(MovieDtoFactory.class)) {
            dtoFactoryMock.when(MovieDtoFactory::nullInstance).thenReturn(nullMovieDto);

            // Act
            StarshipDtoNullable result = StarshipMapper.toDto(entity);

            // Assert
            assertThat(result.id()).isEqualTo(ID);
            assertThat(result.name()).isEqualTo(NAME);
            assertThat(result.movieDto()).isEqualTo(nullMovieDto);
        }
    }

    @Test
    void toEntityFromDtoWithMovieShouldMapCorrectly() {
        // Arrange
        MovieDto movieDto = new MovieDto(MOVIE_ID, MOVIE_TITLE, MOVIE_YEAR, MOVIE_OSCAR);
        StarshipDto dto = new StarshipDto(ID, NAME, movieDto);
        MovieEntity movieEntity = new MovieEntity(MOVIE_ID, MOVIE_TITLE, MOVIE_YEAR, MOVIE_OSCAR);

        try (MockedStatic<MovieMapper> movieMapperMock = mockStatic(MovieMapper.class)) {
            movieMapperMock.when(() -> MovieMapper.toEntity(movieDto)).thenReturn(movieEntity);

            // Act
            StarshipEntity result = StarshipMapper.toEntity(dto);

            // Assert
            assertThat(result.getId()).isEqualTo(ID);
            assertThat(result.getName()).isEqualTo(NAME);
            assertThat(result.getMovie()).isEqualTo(movieEntity);
        }
    }

    @Test
    void toEntityFromDtoWithNullMovieShouldHandleNullMovie() {
        // Arrange
        StarshipDtoNullable dto = new StarshipDto(ID, NAME, MovieDtoFactory.nullInstance());

        try (MockedStatic<MovieMapper> movieMapperMock = mockStatic(MovieMapper.class)) {

            // Act
            StarshipEntity result = StarshipMapper.toEntity(dto);

            // Assert
            assertThat(result.getId()).isEqualTo(ID);
            assertThat(result.getName()).isEqualTo(NAME);
            assertThat(result.getMovie()).isNull();
        }
    }

    @Test
    void toEntityFromAddRequestShouldMapCorrectly() {
        // Arrange
        StarshipAddRequest request = new StarshipAddRequest(NAME, MOVIE_ID, 
            "New Starwars", 2025, false);

        // Act
        StarshipEntity result = StarshipMapper.toEntity(request);

        // Assert
        assertThat(result.getName()).isEqualTo(NAME);
        assertThat(result.getMovie().getId()).isEqualTo(MOVIE_ID);
        assertThat(result.getMovie().getTitle()).isNull(); // Only ID is set from request
    }
}