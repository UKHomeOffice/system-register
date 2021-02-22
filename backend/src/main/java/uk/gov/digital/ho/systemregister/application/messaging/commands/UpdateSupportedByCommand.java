package uk.gov.digital.ho.systemregister.application.messaging.commands;

import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.EntityName;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_SystemEvent;
import uk.gov.digital.ho.systemregister.application.messaging.events.SupportedByUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;

public class UpdateSupportedByCommand implements UpdateCommand {
    private final int id;
    @EntityName
    private final String supportedBy;
    @NotNull
    private final SR_Person author;
    @NotNull
    private final Instant timestamp;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public UpdateSupportedByCommand(int id, String supportedBy, SR_Person author, Instant timestamp) {
        this.id = id;
        this.supportedBy = supportedBy != null ? supportedBy.trim() : null;
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
        return new SupportedByUpdatedEvent(id, supportedBy, author, timestamp);
    }

    @Override
    public void ensureCommandUpdatesSystem(SR_System system) throws CommandHasNoEffectException {
        if (!willUpdate(system)) {
            throw new CommandHasNoEffectException("supporter is the same: " + supportedBy);
        }
    }

    public boolean willUpdate(SR_System system) {
        return !Objects.equals(supportedBy, system.supportedBy);
    }
}
