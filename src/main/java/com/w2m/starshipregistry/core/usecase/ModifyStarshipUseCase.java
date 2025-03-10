package com.w2m.starshipregistry.core.usecase;

import org.springframework.stereotype.Service;

import com.w2m.starshipregistry.core.dto.StarshipDtoNullable;
import com.w2m.starshipregistry.core.dto.StarshipUpdateRequest;
import com.w2m.starshipregistry.core.ports.inbound.ModifyStarshipPort;
import com.w2m.starshipregistry.core.ports.outbound.AlertDataModificationPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ModifyStarshipUseCase implements ModifyStarshipPort {

    private final AlertDataModificationPort alertDataModificationPort;
    private final ModifyStarshipFromMqUseCase modifyStarshipFromMqUseCase;

    @Override
    public StarshipDtoNullable execute(Long id, StarshipUpdateRequest updateRequest) {
        StarshipDtoNullable starshipDtoSaved = modifyStarshipFromMqUseCase.execute(id, updateRequest);
        alertDataModificationPort.notifyStarshipModification(id, updateRequest.name(), updateRequest.movieId());
        return starshipDtoSaved;
    }
}
