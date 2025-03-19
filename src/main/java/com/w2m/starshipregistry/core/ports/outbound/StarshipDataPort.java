package com.w2m.starshipregistry.core.ports.outbound;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.w2m.starshipregistry.core.dto.StarshipAddRequest;
import com.w2m.starshipregistry.core.dto.StarshipDtoNullable;


public interface StarshipDataPort {
    Page<StarshipDtoNullable> searchStarshipsByName(String name, Pageable pageable);
    Page<StarshipDtoNullable> findAllStarships(Pageable pageable);
    StarshipDtoNullable findStarshipById(Long id);
    boolean existsByName(String name);
    StarshipDtoNullable addNewStarship(StarshipAddRequest starshipAddRequest);
    StarshipDtoNullable save(StarshipDtoNullable starshipParent);
    void deleteStarship(Long id);
}
