package com.w2m.starshipregistry.core.usecase;

import com.w2m.starshipregistry.core.ports.inbound.ModifyStarshipPort;
import org.springframework.stereotype.Service;

import com.w2m.starshipregistry.core.dtos.StarshipDtoNullable;
import com.w2m.starshipregistry.core.dtos.StarshipUpdateRequest;
import com.w2m.starshipregistry.core.exceptions.StarshipDependencyException;
import com.w2m.starshipregistry.core.exceptions.StarshipDuplicatedException;
import com.w2m.starshipregistry.core.exceptions.StarshipNotFoundException;
import com.w2m.starshipregistry.core.ports.outbound.AlertDataModificationPort;
import com.w2m.starshipregistry.core.ports.outbound.StarshipDataPort;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.starshipdata.exceptions.DependencyConflictException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ModifyStarshipUseCase implements ModifyStarshipPort {

    private final StarshipDataPort starshipDataPort;
    private final AlertDataModificationPort alertDataModificationPort;

	@Override
    public StarshipDtoNullable execute(Long id, StarshipUpdateRequest updateRequest) {

        StarshipDtoNullable starshipDto = starshipDataPort.findStarshipById(id);
        if(starshipDto.isNull()){
            throw new StarshipNotFoundException(id);
        }

        // Verify if starship with the same name already exists"
        if (!starshipDto.name().equals(updateRequest.name())
            && starshipDataPort.existsByName(updateRequest.name())) {
            throw new StarshipDuplicatedException(id);
        }
        // Update Starship field
        starshipDto = starshipDto.updateName(updateRequest.name());
        starshipDto = starshipDto.updateMovieId(updateRequest.movieId());
        StarshipDtoNullable starshipDtoSaved;
        try{
            starshipDtoSaved = starshipDataPort.save(starshipDto);
        } catch (DependencyConflictException e) {
            throw new StarshipDependencyException("Starship with ID %d cannot be modify due to existing dependencies".formatted(id), e);
        }
        alertDataModificationPort.notifyStarshipModification(id, updateRequest.name(), updateRequest.movieId());

        return starshipDtoSaved;

    }
}