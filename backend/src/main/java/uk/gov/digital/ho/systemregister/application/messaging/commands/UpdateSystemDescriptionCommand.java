package uk.gov.digital.ho.systemregister.application.messaging.commands;

import com.google.common.base.Objects;
import uk.gov.digital.ho.systemregister.application.messaging.events.ProductOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemDescriptionUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UpdateSystemDescriptionCommand {
    public final int id;
    @Size(min = 2, message = "You must enter a description or leave blank if you do not know it.")
    private final String description;
    @NotNull
    public final SR_Person author;
    @NotNull
    public final Instant timestamp;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public UpdateSystemDescriptionCommand(int id, String description, SR_Person author, Instant timestamp) {
        this.id = id;
        this.description = description == null ? null : description.trim();
        this.author = author;
        this.timestamp = timestamp;
    }

    public SystemDescriptionUpdatedEvent toEvent() {
        return new SystemDescriptionUpdatedEvent(id, description, author, timestamp);
    }

    public boolean willUpdate(SR_System system) {
        return !Objects.equal(description, system.description);
    }
}
