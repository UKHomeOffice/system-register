package uk.gov.digital.ho.systemregister.io.database;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.model.Snapshot;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface IEventStore {
    Optional<Snapshot> getSnapshot();

    Optional<List<SR_Event>> getEvents();

    Optional<List<SR_Event>> getEvents(Instant from);

    void save(Snapshot snapshot);

    void save(SR_Event evt);
}
