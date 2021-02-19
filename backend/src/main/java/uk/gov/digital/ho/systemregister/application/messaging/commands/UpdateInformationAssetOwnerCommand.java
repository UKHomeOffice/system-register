package uk.gov.digital.ho.systemregister.application.messaging.commands;

import com.google.common.base.Objects;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.ContactName;
import uk.gov.digital.ho.systemregister.application.messaging.events.InformationAssetOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import javax.validation.constraints.NotNull;
import java.time.Instant;

public class UpdateInformationAssetOwnerCommand implements Command{
    private final int id;
    @ContactName
    private final String informationAssetOwner;
    @NotNull
    private final SR_Person author;
    @NotNull
    private final Instant timestamp;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public UpdateInformationAssetOwnerCommand(int id, String informationAssetOwner, SR_Person author, Instant timestamp) {
        this.id = id;
        this.informationAssetOwner = informationAssetOwner == null ? null : informationAssetOwner.trim();
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

    public InformationAssetOwnerUpdatedEvent toEvent() {
        return new InformationAssetOwnerUpdatedEvent(id, informationAssetOwner, author, timestamp);
    }

    @Override
    public void ensureCommandUpdatesSystem(SR_System system) throws CommandHasNoEffectException {
        if (!willUpdate(system)) {
            throw new CommandHasNoEffectException("information asset owner is the same: " + informationAssetOwner);
        }
    }

    public boolean willUpdate(SR_System system) {
        return !Objects.equal(informationAssetOwner, system.informationAssetOwner);
    }
}
