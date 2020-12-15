package uk.gov.digital.ho.systemregister.application.messaging.commands;

import com.google.common.base.Objects;
import uk.gov.digital.ho.systemregister.application.messaging.events.CriticalityUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import javax.validation.constraints.NotNull;
import java.time.Instant;

public class UpdateCriticalityCommand {
    public final int id;
    //TODO add validation so that this field can only have certain values (poss not needed if enum)
    private final String criticality;
    @NotNull
    public final SR_Person author;
    @NotNull
    public final Instant timestamp;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public UpdateCriticalityCommand(SR_Person author, Instant timestamp, int id, String criticality) {
        this.id = id;
        this.criticality = criticality == null ? null : criticality.trim();
        this.author = author;
        this.timestamp = timestamp;
    }

    public CriticalityUpdatedEvent toEvent() {
        return new CriticalityUpdatedEvent(author, timestamp, id, criticality);
    }

    public boolean willUpdate(SR_System system) {
        return !Objects.equal(criticality, system.criticality);
    }

}
