package com.w2m.starshipregistry.core.usecase;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.w2m.starshipregistry.core.dto.StarshipDtoNullable;
import com.w2m.starshipregistry.core.ports.inbound.FindByNameStarshipPort;
import com.w2m.starshipregistry.core.ports.outbound.StarshipDataPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FindByNameStarshipUseCase implements FindByNameStarshipPort {
    private final StarshipDataPort starshipData;

    // Scenario: Search for starships by name
	@Override
    public Page<StarshipDtoNullable> execute(String name, Pageable pageable) {
        return starshipData.searchStarshipsByName(name, pageable);
    }


}
