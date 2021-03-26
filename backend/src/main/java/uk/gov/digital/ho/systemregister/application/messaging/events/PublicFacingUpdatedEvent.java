package uk.gov.digital.ho.systemregister.application.messaging.events;

import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;

public class PublicFacingUpdatedEvent extends SR_SystemEvent {
    public final int id;
    public final String publicFacing;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public PublicFacingUpdatedEvent(int id, String publicFacing, SR_Person author, Instant timestamp) {
        super(author, timestamp);
        this.id = id;
        this.publicFacing = publicFacing;
    }

    @Override
    public SR_System update(SR_System system) {
        return system.withPublicFacing(publicFacing);
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
