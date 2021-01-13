package uk.gov.digital.ho.systemregister.application.messaging.commands;

import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_SystemEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;

public interface Command {
    int getId();
    SR_Person getAuthor();
    Instant getTimestamp();

    SR_SystemEvent toEvent();

    void ensureCommandUpdatesSystem(SR_System system) throws CommandHasNoEffectException;
}
