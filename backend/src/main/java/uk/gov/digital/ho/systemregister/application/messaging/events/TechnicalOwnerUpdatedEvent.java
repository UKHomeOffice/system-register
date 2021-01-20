package uk.gov.digital.ho.systemregister.application.messaging.events;

import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;

public class TechnicalOwnerUpdatedEvent extends SR_SystemEvent {
    private final int id;
    private final String technicalOwner;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public TechnicalOwnerUpdatedEvent(int id, String technicalOwner, SR_Person author, Instant timestamp) {
        super(author, timestamp);
        this.id = id;
        this.technicalOwner = technicalOwner;
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
        return system.withTechnicalOwner(technicalOwner);
    }
}
