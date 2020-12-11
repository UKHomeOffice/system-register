package uk.gov.digital.ho.systemregister.application.messaging.events;

import uk.gov.digital.ho.systemregister.domain.SR_Person;

import java.time.Instant;

public abstract class SR_SystemEvent extends SR_Event implements SystemUpdater {
    @SuppressWarnings("CdiInjectionPointsInspection")
    public SR_SystemEvent(SR_Person author, Instant timestamp) {
        super(author, timestamp);
    }

    public abstract int getSystemId();
    public abstract Instant getUpdateTimestamp();
}
