package uk.gov.digital.ho.systemregister.application.messaging.events;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;

public class SystemNameUpdatedEvent extends SR_SystemEvent {
    public final int id;
    public final String name;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public SystemNameUpdatedEvent(int id, String name, SR_Person author, Instant timestamp) {
        super(author, timestamp);
        this.id = id;
        this.name = name;
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
        return system.withName(name);
    }
}
