package uk.gov.digital.ho.systemregister.application.messaging.commands;

import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.SystemDescription;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemDescriptionUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;

public class UpdateSystemDescriptionCommand implements Command {
    private final int id;
    @SystemDescription
    private final String description;
    @NotNull
    private final SR_Person author;
    @NotNull
    private final Instant timestamp;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public UpdateSystemDescriptionCommand(int id, String description, SR_Person author, Instant timestamp) {
        this.id = id;
        this.description = description == null ? null : description.trim();
        this.author = author;
        this.timestamp = timestamp;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public SR_Person getAuthor() {
        return author;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    public SystemDescriptionUpdatedEvent toEvent() {
        return new SystemDescriptionUpdatedEvent(id, description, author, timestamp);
    }

    @Override
    public void ensureCommandUpdatesSystem(SR_System system) throws CommandHasNoEffectException {
        if (!willUpdate(system)) {
            throw new CommandHasNoEffectException("system description is the same: " + description);
        }
    }

    public boolean willUpdate(SR_System system) {
        return !Objects.equals(description, system.description);
    }
}
