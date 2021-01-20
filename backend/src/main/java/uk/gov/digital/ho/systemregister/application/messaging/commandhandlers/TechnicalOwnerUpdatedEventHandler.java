package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.EventHandler;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_SystemEvent;

public class TechnicalOwnerUpdatedEventHandler implements EventHandler {
    @Override
    public void handle(SR_SystemEvent event) {
        throw new UnsupportedOperationException();
    }
}
