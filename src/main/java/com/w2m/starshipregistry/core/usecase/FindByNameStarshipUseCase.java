package com.w2m.starshipregistry.core.usecase;

import java.util.List;

import com.w2m.starshipregistry.core.dto.StarshipDtoNullable;
import com.w2m.starshipregistry.core.ports.inbound.FindByNameStarshipPort;
import org.springframework.stereotype.Service;

import com.w2m.starshipregistry.core.ports.outbound.StarshipDataPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FindByNameStarshipUseCase implements FindByNameStarshipPort {
    private final StarshipDataPort starshipData;

    // Scenario: Search for starships by name
	@Override
    public List<StarshipDtoNullable> execute(String name) {
        return starshipData.searchStarshipsByName(name);
    }


}
