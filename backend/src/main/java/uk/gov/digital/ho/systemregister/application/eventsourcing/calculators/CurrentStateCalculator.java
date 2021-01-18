package uk.gov.digital.ho.systemregister.application.eventsourcing.calculators;

import io.smallrye.mutiny.tuples.Tuple2;
import io.smallrye.mutiny.tuples.Tuple3;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.model.Snapshot;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_SystemEvent;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemUpdater;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@ApplicationScoped
public class CurrentStateCalculator {
    public CurrentState crunch(Snapshot snapshot, List<SR_Event> events) {
        var registerLastUpdatedAt = snapshot.timestamp;
        var systemsById = groupExistingSystemsById(snapshot);
        var eventsBySystemId = groupSystemEventsBySystemId(events, registerLastUpdatedAt);

        for (var eventsForSystem : eventsBySystemId.entrySet()) {
            var systemId = eventsForSystem.getKey();
            var systemEvents = eventsForSystem.getValue();

            var lastUpdatedTimestamp = computeUpdatedSystem(systemId, systemEvents, systemsById);
            if (lastUpdatedTimestamp.isAfter(registerLastUpdatedAt)) {
                registerLastUpdatedAt = lastUpdatedTimestamp;
            }
        }

        return theCurrentRegisterState(systemsById, registerLastUpdatedAt);
    }

    public SR_System applyUpdateToSystem(SR_System system, SystemUpdater updater) {
        return updater.update(system);
    }

    private Instant computeUpdatedSystem(int systemId, List<SR_SystemEvent> events, Map<Integer, Tuple3<SR_System, SR_Person, Instant>> systemsById) {
        var sortedEvents = events.stream()
                .sorted(comparing(event -> event.timestamp))
                .collect(toList());

        systemsById.compute(
                systemId,
                (id, systemAndMetadata) -> {
                    var system = systemAndMetadata != null
                            ? systemAndMetadata.getItem1()
                            : null;
                    return applyEvents(system, sortedEvents);
                });

        return sortedEvents.get(sortedEvents.size() - 1).timestamp;
    }

    private Map<Integer, Tuple3<SR_System, SR_Person, Instant>> groupExistingSystemsById(Snapshot snapshot) {
        return snapshot.systems.stream()
                .collect(toMap(
                        system -> system.id,
                        system -> Tuple3.of(system, null, system.lastUpdated)));
    }

    private Map<Integer, List<SR_SystemEvent>> groupSystemEventsBySystemId(List<SR_Event> events, Instant timeOfSnapshot) {
        return events.stream()
                .filter(SR_SystemEvent.class::isInstance)
                .map(SR_SystemEvent.class::cast)
                .filter(event -> event.isAfter(timeOfSnapshot))
                .collect(groupingBy(SR_SystemEvent::getSystemId));
    }

    private Tuple3<SR_System, SR_Person, Instant> applyEvents(SR_System system, List<SR_SystemEvent> events) {
        var currentSystem = system;
        for (var event : events) {
            currentSystem = event.update(currentSystem);
        }
        var lastEvent = events.get(events.size() - 1);
        return Tuple3.of(currentSystem, lastEvent.author, lastEvent.getUpdateTimestamp());
    }

    private CurrentState theCurrentRegisterState(Map<Integer, Tuple3<SR_System, SR_Person, Instant>> systemsById, Instant registerLastUpdatedAt) {
        return new CurrentState(
                systemsById.values().stream()
                        .collect(toMap(
                                Tuple2::getItem1,
                                systemsAndMetadata -> new UpdateMetadata(
                                        systemsAndMetadata.getItem2(),
                                        systemsAndMetadata.getItem3()))),
                registerLastUpdatedAt);
    }
}
