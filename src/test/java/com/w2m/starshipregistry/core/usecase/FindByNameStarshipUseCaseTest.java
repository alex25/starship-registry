package com.w2m.starshipregistry.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.w2m.starshipregistry.core.dto.MovieDtoNullable;
import com.w2m.starshipregistry.core.dto.StarshipDtoNullable;
import com.w2m.starshipregistry.core.dto.factories.MovieDtoFactory;
import com.w2m.starshipregistry.core.dto.factories.StarshipDtoFactory;
import com.w2m.starshipregistry.core.ports.outbound.StarshipDataPort;

@ExtendWith(MockitoExtension.class)
public class FindByNameStarshipUseCaseTest {

    @Mock
    private StarshipDataPort starshipDataPort;

    @InjectMocks
    private FindByNameStarshipUseCase findByNameStarshipUseCase;

    private static final String TEST_NAME = "Millennium Falcon";
    private StarshipDtoNullable existingStarshipDto;

    @BeforeEach
    void setUp() {
        MovieDtoNullable existingMovie = MovieDtoFactory.create(1L, "star wars", 1987, false);
        existingStarshipDto = StarshipDtoFactory.create(1L, TEST_NAME, existingMovie);
    }

    @Test
    void executeShouldReturnStarshipsByName() {
        // Arrange
        List<StarshipDtoNullable> expectedList = List.of(existingStarshipDto);
        
        when(starshipDataPort.searchStarshipsByName(TEST_NAME))
            .thenReturn(expectedList);

        // Act
        List<StarshipDtoNullable> result = findByNameStarshipUseCase.execute(TEST_NAME);

        // Assert
        assertThat(result).isEqualTo(expectedList);
        verify(starshipDataPort).searchStarshipsByName(eq(TEST_NAME));
    }

    @Test
    void executeWhenNoResultsFoundShouldReturnEmptyList() {
        // Arrange
        when(starshipDataPort.searchStarshipsByName(TEST_NAME))
            .thenReturn(Collections.emptyList());

        // Act
        List<StarshipDtoNullable> result = findByNameStarshipUseCase.execute(TEST_NAME);

        // Assert
        assertThat(result).isEmpty();
        verify(starshipDataPort).searchStarshipsByName(eq(TEST_NAME));
    }
}