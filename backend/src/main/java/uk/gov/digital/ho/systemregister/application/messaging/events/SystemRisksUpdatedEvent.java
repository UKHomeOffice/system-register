package uk.gov.digital.ho.systemregister.application.messaging.events;

import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_Risk;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;
import java.util.List;

public class SystemRisksUpdatedEvent extends SR_SystemEvent {
    public final int id;
    public final List<SR_Risk> risks;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public SystemRisksUpdatedEvent(int id, List<SR_Risk> risks, SR_Person author, Instant timestamp) {
        super(author, timestamp);
        this.id = id;
        this.risks = risks;
    }

    @Override
    public SR_System update(SR_System system) {
        return system.withRisks(risks);
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
