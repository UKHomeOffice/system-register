package uk.gov.digital.ho.systemregister.application.messaging.events;

import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;

public class InformationAssetOwnerUpdatedEvent extends SR_SystemEvent {
    public final int id;
    public final String informationAssetOwner;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public InformationAssetOwnerUpdatedEvent(int id, String informationAssetOwner, SR_Person author, Instant timestamp) {
        super(author, timestamp);
        this.id = id;
        this.informationAssetOwner = informationAssetOwner;
    }

    @Override
    public SR_System update(SR_System system) {
        return system.withInformationAssetOwner(informationAssetOwner);
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
