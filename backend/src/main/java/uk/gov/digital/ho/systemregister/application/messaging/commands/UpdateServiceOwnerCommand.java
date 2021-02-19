package uk.gov.digital.ho.systemregister.application.messaging.commands;

import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.ContactName;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_SystemEvent;
import uk.gov.digital.ho.systemregister.application.messaging.events.ServiceOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;

public class UpdateServiceOwnerCommand implements Command {
    private final int id;
    @ContactName
    private final String serviceOwner;
    @NotNull
    private final SR_Person author;
    @NotNull
    private final Instant timestamp;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public UpdateServiceOwnerCommand(int id, String serviceOwner, SR_Person author, Instant timestamp) {
        this.id = id;
        this.serviceOwner = serviceOwner != null ? serviceOwner.trim() : null;
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
    public SR_SystemEvent toEvent() {
        return new ServiceOwnerUpdatedEvent(id, serviceOwner, author, timestamp);
    }

    @Override
    public void ensureCommandUpdatesSystem(SR_System system) throws CommandHasNoEffectException {
        if (!willUpdate(system)) {
            throw new CommandHasNoEffectException("service owner is the same: " + serviceOwner);
        }
    }

    public boolean willUpdate(SR_System system) {
        return !Objects.equals(serviceOwner, system.serviceOwner);
    }
}
