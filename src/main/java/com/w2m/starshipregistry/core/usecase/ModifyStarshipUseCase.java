package com.w2m.starshipregistry.core.usecase;

import org.springframework.stereotype.Service;

import com.w2m.starshipregistry.core.dtos.StarshipDtoNullable;
import com.w2m.starshipregistry.core.dtos.StarshipUpdateRequest;
import com.w2m.starshipregistry.core.ports.inbound.ModifyStarshipPort;
import com.w2m.starshipregistry.core.ports.outbound.AlertDataModificationPort;
import com.w2m.starshipregistry.core.ports.outbound.StarshipDataPort;

@Service
public class ModifyStarshipUseCase implements ModifyStarshipPort {

    private final AlertDataModificationPort alertDataModificationPort;
    private ModifyStarshipFromMqUseCase modifyStarshipUseCase;

    public ModifyStarshipUseCase(StarshipDataPort starshipDataPort,
            AlertDataModificationPort alertDataModificationPort) {
                modifyStarshipUseCase = new ModifyStarshipFromMqUseCase(starshipDataPort);
        this.alertDataModificationPort = alertDataModificationPort;
    }

    @Override
    public StarshipDtoNullable execute(Long id, StarshipUpdateRequest updateRequest) {
        StarshipDtoNullable starshipDtoSaved = modifyStarshipUseCase.execute(id, updateRequest);
        alertDataModificationPort.notifyStarshipModification(id, updateRequest.name(), updateRequest.movieId());
        return starshipDtoSaved;
    }
}
