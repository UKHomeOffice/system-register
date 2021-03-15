package uk.gov.digital.ho.systemregister.application.messaging.events;

import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_Sunset;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;

public class SunsetUpdatedEvent extends SR_SystemEvent {
    private final int id;
    public final SR_Sunset sunset;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public SunsetUpdatedEvent(int id, SR_Sunset sunset, SR_Person author, Instant timestamp) {
        super(author, timestamp);
        this.id = id;
        this.sunset = sunset;
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
        return system.withSunset(sunset);
    }
}
