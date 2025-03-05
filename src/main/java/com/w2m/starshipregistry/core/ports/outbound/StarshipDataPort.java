package com.w2m.starshipregistry.core.ports.outbound;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.w2m.starshipregistry.core.dtos.StarshipAddRequest;
import com.w2m.starshipregistry.core.dtos.StarshipDtoNullable;


public interface StarshipDataPort {
    List<StarshipDtoNullable> searchStarshipsByName(String name);
    Page<StarshipDtoNullable> findAllStarships(Pageable pageable);
    StarshipDtoNullable findStarshipById(Long id);
    boolean existsByName(String name);
    StarshipDtoNullable addNewStarship(StarshipAddRequest starshipAddRequest);
    StarshipDtoNullable save(StarshipDtoNullable starshipParent);
    void deleteStarship(Long id);
}
