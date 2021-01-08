package uk.gov.digital.ho.systemregister.application.messaging.events;

import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;

public class SystemDescriptionUpdatedEvent extends SR_SystemEvent {
    public final int id;
    public final String description;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public SystemDescriptionUpdatedEvent(int id, String description, SR_Person author, Instant timestamp) {
        super(author, timestamp);
        this.id = id;
        this.description = description;
    }

    @Override
    public SR_System update(SR_System system) {
        return system.withDescription(description);
    }

    @Override
    public int getSystemId() {
        return id;
    }

    @Override
    public Instant getUpdateTimestamp() {
        return timestamp;
    }
}
