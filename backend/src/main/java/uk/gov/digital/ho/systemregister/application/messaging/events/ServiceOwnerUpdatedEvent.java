package uk.gov.digital.ho.systemregister.application.messaging.events;

import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;

public class ServiceOwnerUpdatedEvent extends SR_SystemEvent {
    private final int id;
    public final String serviceOwner;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public ServiceOwnerUpdatedEvent(int id, String serviceOwner, SR_Person author, Instant timestamp) {
        super(author, timestamp);
        this.id = id;
        this.serviceOwner = serviceOwner;
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
        return system.withServiceOwner(serviceOwner);
    }
}
