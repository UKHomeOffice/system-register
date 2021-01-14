package uk.gov.digital.ho.systemregister.application.messaging.eventhandlers;

import uk.gov.digital.ho.systemregister.application.messaging.events.SR_SystemEvent;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemDescriptionUpdatedEvent;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped
public class SystemDescriptionUpdatedEventHandler implements EventHandler {
    private final IEventStore eventStore;

    public SystemDescriptionUpdatedEventHandler(@Named("postgres") IEventStore eventStore) {
        this.eventStore = eventStore;
    }

    public void handle(SR_SystemEvent event) {
        eventStore.save(event);
    }
}
