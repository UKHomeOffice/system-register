package uk.gov.digital.ho.systemregister.application.messaging.events;

import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;

public class CriticalityUpdatedEvent extends SR_SystemEvent {
    public final int id;
    public final String criticality;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public CriticalityUpdatedEvent(SR_Person author, Instant timestamp, int id, String criticality) {
        super(author, timestamp);
        this.id = id;
        this.criticality = criticality;
    }

    @Override
    public int getSystemId() {
        return id;
    }

    @Override
    public Instant getUpdateTimestamp() {
        return timestamp;
    }

    @Override
    public SR_System update(SR_System system) {
        return system.withCriticality(criticality);
    }
}
