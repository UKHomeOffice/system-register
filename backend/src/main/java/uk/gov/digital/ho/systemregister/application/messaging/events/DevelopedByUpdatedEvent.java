package uk.gov.digital.ho.systemregister.application.messaging.events;

import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;

public class DevelopedByUpdatedEvent extends SR_SystemEvent {
    public final int id;
    public final String developedBy;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public DevelopedByUpdatedEvent(int id, String developedBy, SR_Person author, Instant timestamp) {
        super(author, timestamp);
        this.id = id;
        this.developedBy = developedBy;
    }

    @Override
    public SR_System update(SR_System system) {
        return system.withDevelopedBy(developedBy);
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
