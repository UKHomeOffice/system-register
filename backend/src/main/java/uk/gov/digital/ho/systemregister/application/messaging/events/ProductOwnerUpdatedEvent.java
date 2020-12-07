package uk.gov.digital.ho.systemregister.application.messaging.events;

import uk.gov.digital.ho.systemregister.domain.SR_Person;

import java.time.Instant;

public class ProductOwnerUpdatedEvent extends SR_Event {

    public final int id;
    public final String productOwner;

    public ProductOwnerUpdatedEvent(int id, String productOwner, SR_Person author, Instant timestamp) {
        super(author, timestamp);
        this.id = id;
        this.productOwner = productOwner;
    }
}

