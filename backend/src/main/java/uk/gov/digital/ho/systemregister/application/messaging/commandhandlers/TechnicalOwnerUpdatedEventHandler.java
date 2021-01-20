package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.EventHandler;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_SystemEvent;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped
public class TechnicalOwnerUpdatedEventHandler implements EventHandler {
    private final IEventStore eventStore;

    public TechnicalOwnerUpdatedEventHandler(@Named("postgres") IEventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public void handle(SR_SystemEvent event) {
        eventStore.save(event);
    }
}
