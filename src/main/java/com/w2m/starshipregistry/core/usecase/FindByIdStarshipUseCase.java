package com.w2m.starshipregistry.core.usecase;

import com.w2m.starshipregistry.core.ports.inbound.FindByIdStarshipPort;
import org.springframework.stereotype.Service;

import com.w2m.starshipregistry.core.dtos.StarshipDtoNullable;
import com.w2m.starshipregistry.core.exceptions.StarshipNotFoundException;
import com.w2m.starshipregistry.core.ports.outbound.StarshipDataPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FindByIdStarshipUseCase implements FindByIdStarshipPort {
    private final StarshipDataPort starshipData;

    // Scenario: Successfully find a starship by ID
	@Override
    public StarshipDtoNullable execute(Long id) {
        StarshipDtoNullable starshipDtoNullable = starshipData.findStarshipById(id);

        if(starshipDtoNullable.isNull()){
            throw new StarshipNotFoundException(id);
        }

        return starshipDtoNullable;
    }
}
