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

    public CurrentState crunch(Snapshot snapshot, List<SR_Event> events) {
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
                    updatesBySystem.put(event.system, new UpdateMetadata(event.author, event.system.lastUpdated));
                    latestUpdate.set(event.timestamp);
                });

        return new CurrentState(updatesBySystem, latestUpdate.get());
    }
}
