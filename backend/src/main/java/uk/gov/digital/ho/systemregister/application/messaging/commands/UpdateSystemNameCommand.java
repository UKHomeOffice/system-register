package uk.gov.digital.ho.systemregister.application.messaging.commands;

import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemNameUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Objects;

public class UpdateSystemNameCommand implements Command {
    private final int id;
    @Pattern(regexp = "^[^!£$%^*<>|~\"=]*$", message = "You must not use the following special characters: ! £ $ % ^ * | < > ~ \" =")
    @Size(min = 2, message = "You must enter a complete system name.")
    @NotNull(message = "You must enter a system name")
    @NotEmpty(message = "You must enter a system name")
    public final String name;
    @NotNull
    private final SR_Person author;
    @NotNull
    private final Instant timestamp;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public UpdateSystemNameCommand(int id, String name, SR_Person author, Instant timestamp) {
        this.id = id;
        this.name = name == null ? null : name.trim();
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

    @Override
    public SystemNameUpdatedEvent toEvent() {
        return new SystemNameUpdatedEvent(id, name, author, timestamp);
    }

    public boolean willUpdate(SR_System system) {
        return !Objects.equals(name, system.name);
    }

    @Override
    public void ensureCommandUpdatesSystem(SR_System system) throws CommandHasNoEffectException {
        if (!willUpdate(system)) {
            throw new CommandHasNoEffectException("system name is the same: " + system.name);
        }
    }
}
