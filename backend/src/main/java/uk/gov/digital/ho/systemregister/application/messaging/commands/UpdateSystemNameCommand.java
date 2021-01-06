package uk.gov.digital.ho.systemregister.application.messaging.commands;

import com.google.common.base.Objects;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemNameUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UpdateSystemNameCommand {
    public final int id;
    @Pattern(regexp = "^[^!£$%^*<>|~\"=]*$", message = "You must not use the following special characters: ! £ $ % ^ * | < > ~ \" =")
    @Size(min = 2, message = "The contact name must not be incomplete. Please enter a full contact name or leave blank if you do not know it.")
    private final String name;
    @NotNull
    public final SR_Person author;
    @NotNull
    public final Instant timestamp;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public UpdateSystemNameCommand(int id, String name, SR_Person author, Instant timestamp) {
        this.id = id;
        this.name = name == null ? null : name.trim();
        this.author = author;
        this.timestamp = timestamp;
    }

    public SystemNameUpdatedEvent toEvent() {
        return new SystemNameUpdatedEvent(id, name, author, timestamp);
    }

    public boolean willUpdate(SR_System system) {
        return !Objects.equal(name, system.name);
    }
}
