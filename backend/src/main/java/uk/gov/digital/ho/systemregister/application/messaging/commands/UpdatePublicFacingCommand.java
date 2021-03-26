package uk.gov.digital.ho.systemregister.application.messaging.commands;

import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.PublicFacing;
import uk.gov.digital.ho.systemregister.application.messaging.events.InvestmentStateUpdatedEvent;
import uk.gov.digital.ho.systemregister.application.messaging.events.PublicFacingUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;

public class UpdatePublicFacingCommand implements UpdateCommand {
    private final int id;
    @PublicFacing
    private final String publicFacing;
    @NotNull
    private final SR_Person author;
    @NotNull
    private final Instant timestamp;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public UpdatePublicFacingCommand(int id, String publicFacing, SR_Person author, Instant timestamp) {
        this.id = id;
        this.publicFacing = publicFacing == null ? null : publicFacing.trim();
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

    public PublicFacingUpdatedEvent toEvent() {
        return new PublicFacingUpdatedEvent(id, publicFacing, author, timestamp);
    }

    @Override
    public void ensureCommandUpdatesSystem(SR_System system) throws CommandHasNoEffectException {
        if (!willUpdate(system)) {
            throw new CommandHasNoEffectException("public facing is the same: " + publicFacing);
        }
    }

    public boolean willUpdate(SR_System system) {
        return !Objects.equals(publicFacing, system.publicFacing);
    }
}
