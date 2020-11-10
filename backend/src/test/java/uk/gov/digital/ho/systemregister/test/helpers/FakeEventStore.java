package uk.gov.digital.ho.systemregister.test.helpers;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.model.Snapshot;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeEventStore implements IEventStore {
    List<Snapshot> snapshots = new ArrayList<>();
    List<SR_Event> events = new ArrayList<>();

    @Override
    public Optional<Snapshot> getSnapshot() {
        return snapshots.stream()
                .max(Comparator.comparing(s -> s.timestamp));
    }

    @Override
    public Optional<List<SR_Event>> getEvents() {
        return Optional.of(
                events.stream()
                        .sorted(Comparator.comparing(s -> s.timestamp))
                        .collect(Collectors.toList()));
    }

    @Override
    public Optional<List<SR_Event>> getEvents(Instant from) {
        return Optional.of(
                events.stream()
                        .filter(s -> s.timestamp.compareTo(from) > 0)
                        .sorted(Comparator.comparing(s -> s.timestamp))
                        .collect(Collectors.toList()));
    }

    @Override
    public void save(Snapshot snapshot) {
        snapshots.add(snapshot);
    }

    @Override
    public void save(SR_Event evt) {
        events.add(evt);
    }
}