package com.w2m.starshipregistry.infrastructure.adapters.outbound.starshipdata;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.w2m.starshipregistry.core.ports.outbound.StarshipData;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.entities.StarshipEntity;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.repositories.MovieRepository;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.repositories.StarshipRepository;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.starshipdata.exceptions.DependencyConflictException;
import com.w2m.starshipregistry.infrastructure.dto.StarshipDto;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class StarshipDataAdapter implements StarshipData {

    private final StarshipRepository starshipRepository;

    private final MovieRepository movieRepository;

    @Override
    @Cacheable("starshipsByName")
    public List<StarshipDto> searchStarshipsByName(String name) {
        if (name == null) {
            return Collections.emptyList();
        }
        return starshipRepository.findByNameContainingIgnoreCase(name)
                .stream().map(StarshipDto::new).toList();
    }

    @Override
    @Cacheable(value = "allStarships", key = "{#pageable.pageNumber, #pageable.pageSize}")
    public Page<StarshipDto> findAllStarships(Pageable pageable) {
        return starshipRepository.findAll(pageable)
                .map(StarshipDto::new);
    }

    @Override
    @Cacheable("starshipById")
    public Optional<StarshipDto> findStarshipById(Long id) {
        return starshipRepository.findById(id)
                .map(StarshipDto::new);
    }

    @Override
    public boolean existsByName(String name) {
        return starshipRepository.existsByName(name);
    }

    @Override
    @CacheEvict(value = {"starshipsByName", "starshipById", "allStarships"}, allEntries = true)
    public StarshipEntity addNewStarship(StarshipDto starshipDto) {

        try {
            starshipDto.withMovie(movieRepository.save(starshipDto.movieDto().toEntity()));
            return starshipRepository.save(starshipDto.toEntity());
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Starship with the same name already exists", e);
        }
    }

    @Override
    @CacheEvict(value = {"starshipsByName", "starshipById", "allStarships"}, allEntries = true)
    public StarshipEntity modifyStarship(StarshipDto starshipDto) {

        try {
            return starshipRepository.save(starshipDto.toEntity());
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Starship with the same name already exists", e);
        }
    }

    @Override
    @CacheEvict(value = {"starshipsByName", "starshipById", "allStarships"}, allEntries = true)
    public void deleteStarship(Long id) {
        if (!starshipRepository.existsById(id)) {
            throw new EntityNotFoundException("Starship with ID %d not found".formatted(id));
        }

        try {
            starshipRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DependencyConflictException("Starship with ID %d cannot be deleted due to existing dependencies".formatted(id), e);
        }
    }
}
