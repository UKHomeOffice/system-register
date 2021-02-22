package uk.gov.digital.ho.systemregister.application.messaging.commands;

import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.Criticality;
import uk.gov.digital.ho.systemregister.application.messaging.events.CriticalityUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;

public class UpdateCriticalityCommand implements UpdateCommand {
    private final int id;
    @Criticality
    private final String criticality;
    @NotNull
    private final SR_Person author;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public UpdateCriticalityCommand(int id, String criticality, SR_Person author, Instant timestamp) {
        this.id = id;
        this.criticality = criticality == null ? null : criticality.trim();
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

    @NotNull
    private final Instant timestamp;

    public CriticalityUpdatedEvent toEvent() {
        return new CriticalityUpdatedEvent(author, timestamp, id, criticality);
    }

    @Override
    public void ensureCommandUpdatesSystem(SR_System system) throws CommandHasNoEffectException {
        if (!willUpdate(system)) {
            throw new CommandHasNoEffectException("criticality level is the same: " + criticality);
        }
    }

    public boolean willUpdate(SR_System system) {
        return !Objects.equals(criticality, system.criticality);
    }

}
