package uk.gov.digital.ho.systemregister.application.messaging.commands;

import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.ContactName;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_SystemEvent;
import uk.gov.digital.ho.systemregister.application.messaging.events.TechnicalOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;

public class UpdateTechnicalOwnerCommand implements Command {
    private final int id;
    @ContactName
    private final String technicalOwner;
    @NotNull
    private final SR_Person author;
    @NotNull
    private final Instant timestamp;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public UpdateTechnicalOwnerCommand(int id, String technicalOwner, SR_Person author, Instant timestamp) {
        this.id = id;
        this.technicalOwner = technicalOwner != null ? technicalOwner.trim() : null;
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
        return new TechnicalOwnerUpdatedEvent(id, technicalOwner, author, timestamp);
    }

    @Override
    public void ensureCommandUpdatesSystem(SR_System system) throws CommandHasNoEffectException {
        if (!willUpdate(system)) {
            throw new CommandHasNoEffectException("technical owner is the same: " + technicalOwner);
        }
    }

    public boolean willUpdate(SR_System system) {
        return !Objects.equals(technicalOwner, system.technicalOwner);
    }
}
