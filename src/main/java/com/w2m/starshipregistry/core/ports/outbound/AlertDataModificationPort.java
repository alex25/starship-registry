package com.w2m.starshipregistry.core.ports.outbound;

public interface AlertDataModificationPort {
    void notifyStarshipModification(Long id, String name, Long movieId);
}
