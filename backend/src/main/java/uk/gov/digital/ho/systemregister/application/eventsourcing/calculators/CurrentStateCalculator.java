package uk.gov.digital.ho.systemregister.application.eventsourcing.calculators;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.model.Snapshot;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAddedEvent;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class CurrentStateCalculator {
//    public Snapshot crunch(Snapshot initialSnapshot, List<SR_Event> events) {
//        final Snapshot snapshot = initialSnapshot == null ? Snapshot.empty() : new Snapshot(initialSnapshot.systems, initialSnapshot.timestamp);
//        events.sort(comparing((AuthoredMessage e) -> e.timestamp));
//        events.removeIf(e -> e.timestamp.compareTo(snapshot.timestamp) < 1);
//        for (AuthoredMessage evt : events) {
//            if (evt.getClass() == SystemAddedEvent.class) {
//                snapshot.systems.add(((SystemAddedEvent) evt).system);
//                snapshot.timestamp = evt.timestamp;
//            }
//        }
//        return snapshot;
//    }

    public CurrentState crunch2(Snapshot snapshot, List<SR_Event> events) {
        var updatesBySystem = snapshot.systems
                .stream()
                .collect(toMap(
                        identity(),
                        system -> new UpdateMetadata(null, system.lastUpdated)));
        var latestUpdate = new AtomicReference<>(snapshot.timestamp);

        events.stream()
                .sorted(comparing(event -> event.timestamp))
                .filter(event -> event.timestamp.isAfter(snapshot.timestamp))
                .filter(event -> event instanceof SystemAddedEvent)
                .map(SystemAddedEvent.class::cast)
                .forEach(event -> {
                    updatesBySystem.put(event.system, new UpdateMetadata(event.author, event.timestamp));
                    latestUpdate.set(event.timestamp);
                });

        return new CurrentState(updatesBySystem, latestUpdate.get());
    }
}
