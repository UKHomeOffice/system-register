package uk.gov.digital.ho.systemregister.application.eventsourcing.calculators;

import java.util.Comparator;
import java.util.List;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.model.Snapshot;
import uk.gov.digital.ho.systemregister.application.messaging.AuthoredMessage;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAddedEvent;

public class CurrentStateCalculator {

  public Snapshot crunch(Snapshot initialSnapshot, List<SR_Event> events) {
    final Snapshot snapshot = initialSnapshot == null ? Snapshot.empty() : new Snapshot(initialSnapshot.systems, initialSnapshot.timestamp);
    events.sort(Comparator.comparing((AuthoredMessage e) -> e.timestamp));
    events.removeIf(e -> e.timestamp.compareTo(snapshot.timestamp) < 1);
    for (AuthoredMessage evt : events) {
      if (evt.getClass() == SystemAddedEvent.class) {
        snapshot.systems.add(((SystemAddedEvent) evt).system);
        snapshot.timestamp = evt.timestamp;
      }
    }
    return snapshot;
  }

}
