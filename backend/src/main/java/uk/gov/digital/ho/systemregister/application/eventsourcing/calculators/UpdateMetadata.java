package uk.gov.digital.ho.systemregister.application.eventsourcing.calculators;

import uk.gov.digital.ho.systemregister.domain.SR_Person;

import java.time.Instant;

public class UpdateMetadata {
    public final SR_Person updatedBy;
    public final Instant updatedAt;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public UpdateMetadata(SR_Person person, Instant timestamp) {
        this.updatedBy = person;
        this.updatedAt = timestamp;
    }
}
