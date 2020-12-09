package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import com.google.common.base.Objects;
import uk.gov.digital.ho.systemregister.application.messaging.events.ProductOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;

public class UpdateProductOwnerCommand {
    public final int id;
    private final String productOwner;
    public final SR_Person author;
    public final Instant timestamp;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public UpdateProductOwnerCommand(int id, String productOwner, SR_Person author, Instant timestamp) {
        this.id = id;
        this.productOwner = productOwner;
        this.author = author;
        this.timestamp = timestamp;
    }

    public ProductOwnerUpdatedEvent toEvent() {
        return new ProductOwnerUpdatedEvent(id, productOwner, author, timestamp);
    }

    public boolean willUpdate(SR_System system) {
        return !Objects.equal(productOwner, system.productOwner);
    }
}
