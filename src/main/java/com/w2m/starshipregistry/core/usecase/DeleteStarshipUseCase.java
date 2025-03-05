package com.w2m.starshipregistry.core.usecase;

import com.w2m.starshipregistry.core.ports.inbound.DeleteStarshipPort;
import org.springframework.stereotype.Service;

import com.w2m.starshipregistry.core.exceptions.StarshipDependencyException;
import com.w2m.starshipregistry.core.exceptions.StarshipNotFoundException;
import com.w2m.starshipregistry.core.ports.outbound.StarshipDataPort;
import com.w2m.starshipregistry.infrastructure.adapters.outbound.starshipdata.exceptions.DependencyConflictException;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteStarshipUseCase implements DeleteStarshipPort {

    private final StarshipDataPort starshipData;

	@Override
    public void execute(Long id) {
        try {
            starshipData.deleteStarship(id);
        } catch (EntityNotFoundException e) {
            throw new StarshipNotFoundException(id);
        } catch (DependencyConflictException e) {
            throw new StarshipDependencyException("Starship with ID %d cannot be deleted due to existing dependencies".formatted(id), e);
        }
    }
}