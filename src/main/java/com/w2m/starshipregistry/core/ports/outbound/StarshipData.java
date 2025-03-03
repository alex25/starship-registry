package com.w2m.starshipregistry.core.ports.outbound;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.w2m.starshipregistry.infrastructure.adapters.outbound.database.entities.StarshipEntity;
import com.w2m.starshipregistry.infrastructure.dto.StarshipDto;

public interface StarshipData {
    public List<StarshipDto> searchStarshipsByName(String name);
    Page<StarshipDto> findAllStarships(Pageable pageable);
    Optional<StarshipDto> findStarshipById(Long id);
    boolean existsByName(String name);
    StarshipEntity addNewStarship(StarshipDto starshipDto);
    StarshipEntity modifyStarship(StarshipDto starshipDto);
    void deleteStarship(Long id);
}
