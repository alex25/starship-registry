package com.w2m.starshipregistry.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.w2m.starshipregistry.core.dto.MovieDtoNullable;
import com.w2m.starshipregistry.core.dto.NullStarshipDto;
import com.w2m.starshipregistry.core.dto.StarshipDtoNullable;
import com.w2m.starshipregistry.core.dto.StarshipUpdateRequest;
import com.w2m.starshipregistry.core.dto.factories.MovieDtoFactory;
import com.w2m.starshipregistry.core.dto.factories.StarshipDtoFactory;
import com.w2m.starshipregistry.core.exceptions.StarshipDependencyException;
import com.w2m.starshipregistry.core.exceptions.StarshipDuplicatedException;
import com.w2m.starshipregistry.core.exceptions.StarshipNotFoundException;
import com.w2m.starshipregistry.core.ports.outbound.StarshipDataPort;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.starshipdata.exceptions.DependencyConflictException;

@ExtendWith(MockitoExtension.class)
public class ModifyStarshipFromMqUseCaseTest {

    @Mock
    private StarshipDataPort starshipDataPort;

    @InjectMocks
    private ModifyStarshipFromMqUseCase modifyStarshipFromMqUseCase;

    private static final Long ID = 1L;
    private static final String EXISTING_NAME = "Millennium Falcon";
    private static final Long EXISTING_MOVIE_ID = 2L;
    private static final String NEW_NAME = "X-Wing";
    private static final Long NEW_MOVIE_ID = 3L;

    private StarshipDtoNullable existingStarshipDto;
    private StarshipDtoNullable savedStarshipDto;

    @BeforeEach
    void setUp() {
        MovieDtoNullable existingMovie = MovieDtoFactory.create(EXISTING_MOVIE_ID, "star wars", 1987, false);
        MovieDtoNullable newMovie = MovieDtoFactory.create(NEW_MOVIE_ID, "galatic wars", 1997, false);
        existingStarshipDto = StarshipDtoFactory.create(ID, EXISTING_NAME, existingMovie);
        savedStarshipDto = StarshipDtoFactory.create(ID, NEW_NAME, newMovie);
    }


    @Test
    void executeWhenValidUpdateRequestShouldUpdateAndReturnSavedDto() {
        // Arrange
        StarshipDtoNullable updatedDto = existingStarshipDto.updateName(NEW_NAME).updateMovieId(NEW_MOVIE_ID);

        when(starshipDataPort.findStarshipById(ID)).thenReturn(existingStarshipDto);
        when(starshipDataPort.existsByName(NEW_NAME)).thenReturn(false);
        when(starshipDataPort.save(updatedDto)).thenReturn(savedStarshipDto);

        // Act
        StarshipDtoNullable result = modifyStarshipFromMqUseCase.execute(ID,
                new StarshipUpdateRequest(NEW_NAME, NEW_MOVIE_ID));

        // Assert
        assertThat(result).isEqualTo(savedStarshipDto);
        verify(starshipDataPort).findStarshipById(ID);
        verify(starshipDataPort).existsByName(NEW_NAME);
        verify(starshipDataPort).save(updatedDto);
    }

    @Test
    void executeWhenSameNameShouldSkipDuplicateCheck() {
        // Arrange
        StarshipDtoNullable updatedDto = existingStarshipDto.updateMovieId(NEW_MOVIE_ID);

        when(starshipDataPort.findStarshipById(ID)).thenReturn(existingStarshipDto);
        when(starshipDataPort.save(updatedDto)).thenReturn(savedStarshipDto);

        // Act
        StarshipDtoNullable result = modifyStarshipFromMqUseCase.execute(ID,
                new StarshipUpdateRequest(EXISTING_NAME, NEW_MOVIE_ID));

        // Assert
        assertThat(result).isEqualTo(savedStarshipDto);
        verify(starshipDataPort).findStarshipById(ID);
        verify(starshipDataPort, never()).existsByName(anyString());
        verify(starshipDataPort).save(updatedDto);
    }

    @Test
    void executeWhenStarshipNotFoundShouldThrowException() {
        // Arrange
        when(starshipDataPort.findStarshipById(ID)).thenReturn(new NullStarshipDto());

        // Act & Assert
        assertThatThrownBy(
                () -> modifyStarshipFromMqUseCase.execute(ID, new StarshipUpdateRequest(NEW_NAME, NEW_MOVIE_ID)))
                .isInstanceOf(StarshipNotFoundException.class)
                .hasMessageContaining("Starship with ID 1 not found");

        verify(starshipDataPort).findStarshipById(ID);
        verifyNoMoreInteractions(starshipDataPort);
    }

    @Test
    void executeWhenDuplicateNameShouldThrowException() {
        // Arrange
        when(starshipDataPort.findStarshipById(ID)).thenReturn(existingStarshipDto);
        when(starshipDataPort.existsByName(NEW_NAME)).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(
                () -> modifyStarshipFromMqUseCase.execute(ID, new StarshipUpdateRequest(NEW_NAME, NEW_MOVIE_ID)))
                .isInstanceOf(StarshipDuplicatedException.class)
                .hasMessageContaining("Starship with ID 1 with the same name already exists");

        verify(starshipDataPort).findStarshipById(ID);
        verify(starshipDataPort).existsByName(NEW_NAME);
        verifyNoMoreInteractions(starshipDataPort);
    }

    @Test
    void executeWhenSaveShouldThrowStarshipDependencyException() {
        // Arrange
        StarshipDtoNullable updatedDto = existingStarshipDto.updateName(NEW_NAME).updateMovieId(NEW_MOVIE_ID);

        when(starshipDataPort.findStarshipById(ID)).thenReturn(existingStarshipDto);
        when(starshipDataPort.existsByName(NEW_NAME)).thenReturn(false);
        when(starshipDataPort.save(updatedDto)).thenThrow(
            new DependencyConflictException("DependencyConflict", new Exception()));

        // Act & Assert
        assertThatThrownBy(
                () -> modifyStarshipFromMqUseCase.execute(ID, new StarshipUpdateRequest(NEW_NAME, NEW_MOVIE_ID)))
                .isInstanceOf(StarshipDependencyException.class)
                .hasMessageContaining("Starship with ID 1 cannot be modify due to existing dependencies");

        verify(starshipDataPort).findStarshipById(ID);
        verify(starshipDataPort).existsByName(NEW_NAME);
        verify(starshipDataPort).save(updatedDto);
    }
}