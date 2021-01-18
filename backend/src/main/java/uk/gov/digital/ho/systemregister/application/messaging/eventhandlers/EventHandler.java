package uk.gov.digital.ho.systemregister.application.messaging.eventhandlers;

import uk.gov.digital.ho.systemregister.application.messaging.events.SR_SystemEvent;

public interface EventHandler {
    void handle(SR_SystemEvent event);
}
