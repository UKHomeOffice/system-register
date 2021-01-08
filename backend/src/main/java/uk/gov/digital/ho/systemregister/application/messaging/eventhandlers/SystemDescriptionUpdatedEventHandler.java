package uk.gov.digital.ho.systemregister.application.messaging.eventhandlers;

import uk.gov.digital.ho.systemregister.application.messaging.events.SystemDescriptionUpdatedEvent;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import javax.inject.Named;

public class SystemDescriptionUpdatedEventHandler {
    private final IEventStore eventStore;

    public SystemDescriptionUpdatedEventHandler(@Named("postgres") IEventStore eventStore) {
        this.eventStore = eventStore;
    }

    public void handle(SystemDescriptionUpdatedEvent event) {
        eventStore.save(event);
    }
}
