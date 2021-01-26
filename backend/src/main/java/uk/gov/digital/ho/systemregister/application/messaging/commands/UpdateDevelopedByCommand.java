package uk.gov.digital.ho.systemregister.application.messaging.commands;

import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.events.DevelopedByUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Objects;

public class UpdateDevelopedByCommand implements Command{
    private final int id;
    @Pattern(regexp = "^[^!£$%^*<>|~\"=]*$", message = "You must not use the following special characters: ! £ $ % ^ * | < > ~ \" =")
    @Size(min = 2, message = "The contact name must not be incomplete. Please enter a full contact name or leave blank if you do not know it.")
    private final String developedBy;
    @NotNull
    private final SR_Person author;
    @NotNull
    private final Instant timestamp;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public UpdateDevelopedByCommand(int id, String developedBy, SR_Person author, Instant timestamp) {
        this.id = id;
        this.developedBy = developedBy == null ? null : developedBy.trim();
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

    public DevelopedByUpdatedEvent toEvent() {
        return new DevelopedByUpdatedEvent(id, developedBy, author, timestamp);
    }

    @Override
    public void ensureCommandUpdatesSystem(SR_System system) throws CommandHasNoEffectException {
        if (!willUpdate(system)) {
            throw new CommandHasNoEffectException("developed by is the same: " + developedBy);
        }
    }

    public boolean willUpdate(SR_System system) {
        return !Objects.equals(developedBy, system.developedBy);
    }
}
