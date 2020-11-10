package uk.gov.digital.ho.systemregister.application.messaging.eventhandlers;

import com.google.common.eventbus.Subscribe;
import org.jboss.logging.Logger;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAddedEvent;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

public class SystemAddedEventHandler {
    private static final Logger LOG = Logger.getLogger(SystemAddedEventHandler.class);

    IEventStore eventStore;

    public SystemAddedEventHandler(IEventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Subscribe
    public void handle(SystemAddedEvent evt){
        eventStore.save(evt);
    }
}
