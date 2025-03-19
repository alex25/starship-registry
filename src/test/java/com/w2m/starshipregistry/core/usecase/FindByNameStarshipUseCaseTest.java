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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
        Page<StarshipDtoNullable> expectedStarships = new PageImpl<>(List.of(existingStarshipDto));
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        when(starshipDataPort.searchStarshipsByName(TEST_NAME, pageable))
            .thenReturn(expectedStarships);

        // Act
        Page<StarshipDtoNullable> result = findByNameStarshipUseCase.execute(TEST_NAME, pageable);

        // Assert
        assertThat(result).isEqualTo(expectedStarships);
        verify(starshipDataPort).searchStarshipsByName(eq(TEST_NAME), eq(pageable));
    }

    @Test
    void executeWhenNoResultsFoundShouldReturnEmptyList() {
        Page<StarshipDtoNullable> emptyStarships = new PageImpl<>(Collections.emptyList());
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        when(starshipDataPort.searchStarshipsByName(TEST_NAME, pageable))
            .thenReturn(emptyStarships);

        // Act
        Page<StarshipDtoNullable> result = findByNameStarshipUseCase.execute(TEST_NAME, pageable);

        // Assert
        assertThat(result).isEmpty();
        verify(starshipDataPort).searchStarshipsByName(eq(TEST_NAME), eq(pageable));
    }
}