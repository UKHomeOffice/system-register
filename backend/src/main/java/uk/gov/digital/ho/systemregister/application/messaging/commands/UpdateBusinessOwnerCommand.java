package uk.gov.digital.ho.systemregister.application.messaging.commands;

import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.ContactName;
import uk.gov.digital.ho.systemregister.application.messaging.events.BusinessOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;

public class UpdateBusinessOwnerCommand implements Command {
    private final int id;
    @ContactName
    private final String businessOwner;
    @NotNull
    private final SR_Person author;
    @NotNull
    private final Instant timestamp;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public UpdateBusinessOwnerCommand(int id, String businessOwner, SR_Person author, Instant timestamp) {
        this.id = id;
        this.businessOwner = businessOwner == null ? null : businessOwner.trim();
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

    public BusinessOwnerUpdatedEvent toEvent() {
        return new BusinessOwnerUpdatedEvent(id, businessOwner, author, timestamp);
    }

    @Override
    public void ensureCommandUpdatesSystem(SR_System system) throws CommandHasNoEffectException {
        if (!willUpdate(system)) {
            throw new CommandHasNoEffectException("business owner is the same: " + businessOwner);
        }
    }

    public boolean willUpdate(SR_System system) {
        return !Objects.equals(businessOwner, system.businessOwner);
    }
}
