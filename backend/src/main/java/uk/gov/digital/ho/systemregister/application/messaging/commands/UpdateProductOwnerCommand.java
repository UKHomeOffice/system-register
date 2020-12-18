package uk.gov.digital.ho.systemregister.application.messaging.commands;

import com.google.common.base.Objects;
import uk.gov.digital.ho.systemregister.application.messaging.events.ProductOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UpdateProductOwnerCommand {
    public final int id;
    @Pattern(regexp = "^[^!£$%^*<>|~\"=]*$", message = "You must not use the following special characters: ! £ $ % ^ * | < > ~ \" =")
    @Size(min = 2, message = "The contact name must not be incomplete. Please enter a full contact name or leave blank if you do not know it.")
    private final String productOwner;
    @NotNull
    public final SR_Person author;
    @NotNull
    public final Instant timestamp;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public UpdateProductOwnerCommand(int id, String productOwner, SR_Person author, Instant timestamp) {
        this.id = id;
        this.productOwner = productOwner == null ? null : productOwner.trim();
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
