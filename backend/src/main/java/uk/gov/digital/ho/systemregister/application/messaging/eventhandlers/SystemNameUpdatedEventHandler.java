package uk.gov.digital.ho.systemregister.application.messaging.eventhandlers;

import uk.gov.digital.ho.systemregister.application.messaging.events.SystemNameUpdatedEvent;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped
public class SystemNameUpdatedEventHandler {
        private final IEventStore eventStore;

        public SystemNameUpdatedEventHandler(@Named("postgres") IEventStore eventStore) {
            this.eventStore = eventStore;
        }

        public void handle(SystemNameUpdatedEvent event) {
            eventStore.save(event);
        }
    }


