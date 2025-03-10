package com.w2m.starshipregistry.core.usecase;

import com.w2m.starshipregistry.core.dto.StarshipDtoNullable;
import com.w2m.starshipregistry.core.ports.inbound.FindAllStarshipPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.w2m.starshipregistry.core.ports.outbound.StarshipDataPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FindAllStarshipUseCase implements FindAllStarshipPort {
    private final StarshipDataPort starshipData;

    // Scenario: Retrieve all starships with pagination
	@Override
    public Page<StarshipDtoNullable> execute(Pageable pageable) {
        return starshipData.findAllStarships(pageable);
    }

}
