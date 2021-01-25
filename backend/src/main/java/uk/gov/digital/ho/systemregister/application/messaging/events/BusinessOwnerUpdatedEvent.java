package uk.gov.digital.ho.systemregister.application.messaging.events;

import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;

public class BusinessOwnerUpdatedEvent extends SR_SystemEvent {
    public final int id;
    public final String businessOwner;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public BusinessOwnerUpdatedEvent(int id, String businessOwner, SR_Person author, Instant timestamp) {
        super(author, timestamp);
        this.id = id;
        this.businessOwner = businessOwner;
    }

    @Override
    public SR_System update(SR_System system) {
        return system.withBusinessOwner(businessOwner);
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
