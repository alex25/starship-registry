package com.w2m.starshipregistry.infrastructure.adapters.outbound.starshipdata;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.w2m.starshipregistry.core.dto.StarshipAddRequest;
import com.w2m.starshipregistry.core.dto.StarshipDtoNullable;
import com.w2m.starshipregistry.core.dto.factories.StarshipDtoFactory;
import com.w2m.starshipregistry.core.exceptions.StarshipNotFoundException;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.entities.MovieEntity;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.entities.StarshipEntity;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.repositories.MovieRepository;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.repositories.StarshipRepository;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.starshipdata.exceptions.DependencyConflictException;
import com.w2m.starshipregistry.infrastructure.mappers.MovieMapper;
import com.w2m.starshipregistry.infrastructure.mappers.StarshipMapper;

@ExtendWith(MockitoExtension.class)
public class StarshipDataAdapterTest {

    @InjectMocks
    StarshipDataAdapter adapter;

    @Mock
    StarshipRepository starshipRepository;

    @Mock
    MovieRepository movieRepository;

    @Test
    void findStarshipSuccess() {
        MovieEntity movie = MovieEntity.builder().id(1L).title("Star Wars").build();
        StarshipEntity starship = StarshipEntity.builder().id(1L).name("Enterprise").movie(movie).build();
        Page<StarshipEntity> response = new PageImpl<>(List.of(starship));
        // Arrange
        given(starshipRepository.findByNameContainingIgnoreCase(any(), any())).willReturn(response);

        // Act
        Pageable pageable = PageRequest.of(0, 10);
        Page<StarshipDtoNullable> result = adapter.searchStarshipsByName("Enterprise", pageable);

        // Assert
        assertEquals(List.of(StarshipDtoFactory.create(1L, "Enterprise", MovieMapper.toDto(movie))), result.getContent());
    }

    @Test
    void searchStarshipsByNameWithNullNameSuccess() {
        // Act
        Pageable pageable = PageRequest.of(0, 10);
        Page<StarshipDtoNullable> result = adapter.searchStarshipsByName(null, pageable);

        // Assert
        assertTrue(result.isEmpty());
        verify(starshipRepository, never()).findByNameContainingIgnoreCase(any(), any());
    }

    @Test
    void findAllStarshipsSuccess() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        MovieEntity movie = MovieEntity.builder().id(1L).title("Star Wars").build();
        StarshipEntity starship1 = StarshipEntity.builder().id(1L).name("Enterprise").movie(movie).build();
        StarshipEntity starship2 = StarshipEntity.builder().id(2L).name("Millennium Falcon").movie(movie).build();

        List<StarshipEntity> starships = List.of(starship1, starship2);
        Page<StarshipEntity> starshipPage = new PageImpl<>(starships, pageable, starships.size());

        given(starshipRepository.findAll(pageable)).willReturn(starshipPage);

        // Act
        Page<StarshipDtoNullable> result = adapter.findAllStarships(pageable);

        // Assert
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals(StarshipDtoFactory.create(1L, "Enterprise", MovieMapper.toDto(movie)), result.getContent().get(0));
        assertEquals(StarshipDtoFactory.create(2L, "Millennium Falcon", MovieMapper.toDto(movie)), result.getContent().get(1));
    }

    @Test
    void findStarshipByIdWhenExists() {
        // Arrange
        MovieEntity movie = MovieEntity.builder().id(1L).title("Star Wars").build();
        StarshipEntity starship = StarshipEntity.builder().id(1L).name("Enterprise").movie(movie).build();

        given(starshipRepository.findById(1L)).willReturn(Optional.of(starship));

        // Act
        // Assert
        StarshipDtoNullable result = assertDoesNotThrow(() -> adapter.findStarshipById(1L));
        assertEquals(StarshipDtoFactory.create(1L, "Enterprise", MovieMapper.toDto(movie)), result);
    }

    @Test
    void findStarshipByIdWhenNotExists() {
        // Arrange
        given(starshipRepository.findById(999L)).willReturn(Optional.empty());

        // Act
        Executable exec = () -> {
            adapter.findStarshipById(999L);
        };

        // Assert
        assertDoesNotThrow(exec);
    }

    @Test
    void addNewStarshipSuccess() {
        // Arrange
        MovieEntity movie = MovieEntity.builder().id(1L).title("Star Wars").build();
        StarshipAddRequest starshipAddRequest = new StarshipAddRequest("Enterprise III",
                2L, "Starwars", 1986, false);
        StarshipDtoNullable starshipResultDto = StarshipDtoFactory.create(11L, "Enterprise III", MovieMapper.toDto(movie));
        given(starshipRepository.save(any())).willReturn(StarshipMapper.toEntity(starshipResultDto));

        // Act
        StarshipDtoNullable result = adapter.addNewStarship(starshipAddRequest);

        // Assert
        assertNotNull(result.id());
    }

    @Test
    void addNewStarshipDuplicateKeyException() {
        // Arrange
        StarshipAddRequest starshipAddRequest = new StarshipAddRequest("Enterprise III",
                2L, "Starwars", 1986, false);
        given(starshipRepository.save(any()))
                .willThrow(new DataIntegrityViolationException("Database constraint violation"));

        // Act
        Executable exec = () -> {
            adapter.addNewStarship(starshipAddRequest);
        };

        // Assert
        assertThrows(DependencyConflictException.class, exec);
    }

    @Test
    void modifyStarshipSuccess() {
        // Arrange
        MovieEntity movie = MovieEntity.builder().id(1L).title("Star Wars").build();
        StarshipDtoNullable starshipDto = StarshipDtoFactory.create(1L, "Storm Shadow", MovieMapper.toDto(movie));
        given(starshipRepository.save(any())).willReturn(StarshipMapper.toEntity(starshipDto));

        // Act
        StarshipDtoNullable result = adapter.save(starshipDto);

        // Assert
        assertNotNull(result.id());
    }

    @Test
    void modifyStarshipDuplicateKeyException() {
        // Arrange
        MovieEntity movie = MovieEntity.builder().id(1L).title("Star Wars").build();
        StarshipDtoNullable starshipDto = StarshipDtoFactory.create(0L, "X-Wing", MovieMapper.toDto(movie));
        given(starshipRepository.save(any()))
                .willThrow(new DataIntegrityViolationException("Database constraint violation"));

        // Act
        Executable exec = () -> {
            adapter.save(starshipDto);
        };

        // Assert
        assertThrows(DependencyConflictException.class, exec);
    }

    @Test
    void deleteStarshipSuccess() {

        // Arrange
        given(starshipRepository.existsById(any())).willReturn(true);

        // Act
        Executable exec = () -> {
            adapter.deleteStarship(1L);
        };

        // Assert
        assertThatNoException().isThrownBy(exec::execute);
    }

    @Test
    void deleteStarshipEntityNotFoundException() {
        // Arrange
        given(starshipRepository.existsById(any())).willReturn(false);

        // Act

        // Assert
        assertThrows(StarshipNotFoundException.class, () -> adapter.deleteStarship(20L));
    }

    @Test
    void deleteStarshipDependencyConflictException() {
        // Arrange
        given(starshipRepository.existsById(any())).willReturn(true);
        doThrow(new DataIntegrityViolationException("Database constraint violation"))
                .when(starshipRepository).deleteById(any());
        // Act
        Executable exec = () -> {
            adapter.deleteStarship(20L);
        };

        // Assert
        assertThrows(DependencyConflictException.class, exec,
                "Starship with ID 20 cannot be deleted due to existing dependencies");
    }
}