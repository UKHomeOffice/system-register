package uk.gov.digital.ho.systemregister.io.database;

import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;

import java.time.Instant;
import java.util.List;

import static java.time.Instant.EPOCH;

public interface EventStore {
    void save(SR_Event event) throws EventStoreException;

    default List<SR_Event> getEvents() throws EventStoreException {
        return getEvents(EPOCH);
    }

    List<SR_Event> getEvents(Instant after) throws EventStoreException;
}
