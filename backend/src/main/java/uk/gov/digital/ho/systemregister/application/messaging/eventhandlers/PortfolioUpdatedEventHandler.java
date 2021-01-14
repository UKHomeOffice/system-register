package uk.gov.digital.ho.systemregister.application.messaging.eventhandlers;

import uk.gov.digital.ho.systemregister.application.messaging.events.SR_SystemEvent;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import javax.inject.Named;

public class PortfolioUpdatedEventHandler implements EventHandler {
    private final IEventStore eventStore;

    public PortfolioUpdatedEventHandler(@Named("postgres") IEventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public void handle(SR_SystemEvent event) {
        eventStore.save(event);
    }
}
