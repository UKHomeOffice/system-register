package uk.gov.digital.ho.systemregister.application.messaging.commands;

import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_SystemEvent;
import uk.gov.digital.ho.systemregister.domain.SR_System;

public interface UpdateCommand extends Command {
    int getId();

    SR_SystemEvent toEvent();

    void ensureCommandUpdatesSystem(SR_System system) throws CommandHasNoEffectException;
}
