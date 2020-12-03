package uk.gov.digital.ho.systemregister.application.eventsourcing.calculators;

import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

public class CurrentState {
    private final Map<SR_System, UpdateMetadata> updatesBySystem;
    private final Instant lastUpdatedAt;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public CurrentState(Map<SR_System, UpdateMetadata> updatesBySystem, Instant lastUpdatedAt) {
        this.updatesBySystem = updatesBySystem;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public List<SR_System> getSystems() {
        return List.copyOf(updatesBySystem.keySet());
    }

    public Map<SR_System, UpdateMetadata> getUpdatesBySystem() {
        return unmodifiableMap(updatesBySystem);
    }

    public Instant getLastUpdatedAt() {
        return lastUpdatedAt;
    }
}
