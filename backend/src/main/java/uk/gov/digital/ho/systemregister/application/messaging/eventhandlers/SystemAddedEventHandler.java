package uk.gov.digital.ho.systemregister.application.messaging.eventhandlers;

import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAddedEvent;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped
public class SystemAddedEventHandler {
    private final IEventStore eventStore;

    public SystemAddedEventHandler(@Named("postgres") IEventStore eventStore) {
        this.eventStore = eventStore;
    }

    public void handle(SystemAddedEvent evt){
        eventStore.save(evt);
    }
}
