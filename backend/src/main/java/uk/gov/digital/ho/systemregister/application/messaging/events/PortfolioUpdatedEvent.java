package uk.gov.digital.ho.systemregister.application.messaging.events;

import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;

public class PortfolioUpdatedEvent extends SR_SystemEvent {
    public final int id;
    public final String portfolio;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public PortfolioUpdatedEvent(int id, String investmentState, SR_Person author, Instant timestamp) {
        super(author, timestamp);
        this.id = id;
        this.portfolio = investmentState;
    }

    @Override
    public SR_System update(SR_System system) { return null; }

    @Override
    public int getSystemId() {
        return id;
    }

    @Override
    public Instant getUpdateTimestamp() {
        return timestamp;
    }
}
