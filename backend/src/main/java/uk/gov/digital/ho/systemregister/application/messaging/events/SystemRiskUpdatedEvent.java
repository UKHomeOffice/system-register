package uk.gov.digital.ho.systemregister.application.messaging.events;

import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_Risk;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;

public class SystemRiskUpdatedEvent extends SR_SystemEvent {
    public final int id;
    public final SR_Risk risk;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public SystemRiskUpdatedEvent(int id, SR_Risk risk, SR_Person author, Instant timestamp) {
        super(author, timestamp);
        this.id = id;
        this.risk = risk;
    }

    @Override
    public SR_System update(SR_System system) {
        return system.withRisk(risk);
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
