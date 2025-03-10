package com.w2m.starshipregistry.core.usecase;

import com.w2m.starshipregistry.core.ports.inbound.AddStarshipPort;
import org.springframework.stereotype.Service;

import com.w2m.starshipregistry.core.dto.StarshipAddRequest;
import com.w2m.starshipregistry.core.dto.StarshipDtoNullable;
import com.w2m.starshipregistry.core.exceptions.StarshipDependencyException;
import com.w2m.starshipregistry.core.exceptions.StarshipDuplicatedException;
import com.w2m.starshipregistry.core.ports.outbound.StarshipDataPort;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.starshipdata.exceptions.DependencyConflictException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddStarshipUseCase implements AddStarshipPort {

    private final StarshipDataPort starshipData;

	@Override
    public StarshipDtoNullable execute(StarshipAddRequest starshipAddRequest) {
         // Verify if starship with the same name already exists"
        if (starshipData.existsByName(starshipAddRequest.name())) {
            throw new StarshipDuplicatedException();
        }
        try {
			return starshipData.addNewStarship(starshipAddRequest);
        } catch (DependencyConflictException e) {
            throw new StarshipDependencyException("Starship cannot be add due to existing dependencies", e);
        }
    }
}