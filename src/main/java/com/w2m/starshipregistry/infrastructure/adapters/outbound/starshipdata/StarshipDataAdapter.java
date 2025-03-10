package com.w2m.starshipregistry.infrastructure.adapters.outbound.starshipdata;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.w2m.starshipregistry.core.dto.NullStarshipDto;
import com.w2m.starshipregistry.core.dto.StarshipAddRequest;
import com.w2m.starshipregistry.core.dto.StarshipDtoNullable;
import com.w2m.starshipregistry.core.exceptions.StarshipNotFoundException;
import com.w2m.starshipregistry.core.ports.outbound.StarshipDataPort;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.entities.MovieEntity;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.entities.StarshipEntity;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.repositories.MovieRepository;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.repositories.StarshipRepository;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.starshipdata.exceptions.DependencyConflictException;
import com.w2m.starshipregistry.infrastructure.mappers.MovieMapper;
import com.w2m.starshipregistry.infrastructure.mappers.StarshipMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class StarshipDataAdapter implements StarshipDataPort {

    private static final StarshipDtoNullable NOT_FOUND = new NullStarshipDto();

    private final StarshipRepository starshipRepository;

    private final MovieRepository movieRepository;

    @Override
    @Cacheable("starshipsByName")
    public List<StarshipDtoNullable> searchStarshipsByName(String name) {
        if (name == null) {
            return Collections.emptyList();
        }
        return starshipRepository.findByNameContainingIgnoreCase(name)
                .stream().map(StarshipMapper::toDto).toList();
    }

    @Override
    @Cacheable(value = "allStarships", key = "{#pageable.pageNumber, #pageable.pageSize}")
    public Page<StarshipDtoNullable> findAllStarships(Pageable pageable) {
        return starshipRepository.findAll(pageable)
                .map(StarshipMapper::toDto);
    }

    @Override
    @Cacheable(value = "starshipById", unless = "#result.isNull()")
    public StarshipDtoNullable findStarshipById(Long id) {
        Optional<StarshipEntity> starshipEntity = starshipRepository.findById(id);
        StarshipDtoNullable starshipDtoNullable;
        if (starshipEntity.isEmpty()) {
            starshipDtoNullable = NOT_FOUND;
        } else {
            starshipDtoNullable = StarshipMapper.toDto(starshipEntity.get());
        }
        return starshipDtoNullable;
    }

    @Override
    public boolean existsByName(String name) {
        return starshipRepository.existsByName(name);
    }

    @Override
    @CacheEvict(value = { "starshipsByName", "starshipById", "allStarships" }, allEntries = true)
    public StarshipDtoNullable addNewStarship(StarshipAddRequest starshipAddRequest) {
        MovieEntity movieEntity = MovieMapper.toEntity(starshipAddRequest);
        StarshipEntity starshipEntity = StarshipMapper.toEntity(starshipAddRequest);
        try {
            if (movieEntity.getId() == null) {
                movieEntity = movieRepository.save(movieEntity);
            }
            starshipEntity.setMovie(movieEntity);
            starshipEntity = starshipRepository.save(starshipEntity);
            return StarshipMapper.toDto(starshipEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DependencyConflictException("Starship with ID %d cannot be added due to existing dependencies"
                    .formatted(starshipEntity.getId()), e);
        }
    }

    @Override
    @CacheEvict(value = { "starshipsByName", "starshipById", "allStarships" }, allEntries = true)
    public StarshipDtoNullable save(StarshipDtoNullable starshipDto) {
        try {
            StarshipEntity savedStarship = starshipRepository.save(StarshipMapper.toEntity(starshipDto));
            return StarshipMapper.toDto(savedStarship);
        } catch (DataIntegrityViolationException e) {
            throw new DependencyConflictException(
                    "Starship with ID %d cannot be modified due to existing dependencies".formatted(starshipDto.id()),
                    e);
        }
    }

    @Override
    @CacheEvict(value = { "starshipsByName", "starshipById", "allStarships" }, allEntries = true)
    public void deleteStarship(Long id) {
        if (!starshipRepository.existsById(id)) {
            throw new StarshipNotFoundException(id);
        }

        try {
            starshipRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DependencyConflictException(
                    "Starship with ID %d cannot be deleted due to existing dependencies".formatted(id), e);
        }
    }

}
