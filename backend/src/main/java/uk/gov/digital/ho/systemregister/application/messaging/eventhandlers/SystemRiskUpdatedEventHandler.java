package uk.gov.digital.ho.systemregister.application.messaging.eventhandlers;

import uk.gov.digital.ho.systemregister.application.messaging.events.SR_SystemEvent;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped
public class SystemRiskUpdatedEventHandler implements EventHandler {
    private final IEventStore eventStore;

    public SystemRiskUpdatedEventHandler(@Named("postgres") IEventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public void handle(SR_SystemEvent event) {
        eventStore.save(event);
    }
}
