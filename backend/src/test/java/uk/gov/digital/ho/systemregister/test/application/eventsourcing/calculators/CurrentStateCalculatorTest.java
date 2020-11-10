package uk.gov.digital.ho.systemregister.test.application.eventsourcing.calculators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.test.helpers.builders.SR_SystemBuilder;
import uk.gov.digital.ho.systemregister.test.helpers.builders.SystemAddedEventBuilder;
import uk.gov.digital.ho.systemregister.test.io.database.SnapshotBuilder;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.model.Snapshot;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAddedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_System;

public class CurrentStateCalculatorTest {
    SnapshotBuilder snapshotBuilder = new SnapshotBuilder();
    SystemAddedEventBuilder systemAddedEventBuilder = new SystemAddedEventBuilder();
    SR_SystemBuilder systemBuilder = new SR_SystemBuilder();

    @Test
    public void crunch_no_snapshot_no_events() {
        var calculator = new CurrentStateCalculator();
        Snapshot expected = Snapshot.empty();

        Snapshot actual = calculator.crunch(null, new ArrayList<SR_Event>());

        assertEquals(expected, actual);
    }

    @Test
    public void crunch_existing_snapshot_no_events() {
        var calculator = new CurrentStateCalculator();
        Snapshot expected = snapshotBuilder.withASystem().build();

        Snapshot actual = calculator.crunch(expected, new ArrayList<SR_Event>());

        assertEquals(expected, actual);
        assertEquals(expected.timestamp, actual.timestamp);
    }

    @Test
    public void crunch_no_snapshot_one_SystemAddedEvent() {
        var calculator = new CurrentStateCalculator();
        SystemAddedEvent event = systemAddedEventBuilder.build();
        List<SR_Event> eventLog = Stream.of(event).collect(Collectors.toCollection(ArrayList::new));
        Snapshot expected = snapshotBuilder.withSystem(event.system).build();

        Snapshot actual = calculator.crunch(null, eventLog);

        assertEquals(expected, actual);
        Assertions.assertEquals(event.timestamp, actual.timestamp);
    }

    @Test
    public void crunch_exisitng_snapshot_one_SystemAddedEvent() {
        var calculator = new CurrentStateCalculator();
        SR_System existingSystem = systemBuilder.withName("Ye Olde System").build();
        SystemAddedEvent event = systemAddedEventBuilder.build();
        List<SR_Event> eventLog = Stream.of(event).collect(Collectors.toCollection(ArrayList::new));
        Snapshot expected = snapshotBuilder.withSystems(existingSystem, event.system).build();

        Snapshot actual =
                calculator.crunch(snapshotBuilder.withSystem(existingSystem).build(), eventLog);

        assertEquals(expected, actual);
        Assertions.assertEquals(event.timestamp, actual.timestamp);
    }

    @Test
    public void crunch_existing_snapshot_two_SystemAddedEvents() {
        var calculator = new CurrentStateCalculator();
        SR_System existingSystem = systemBuilder.withName("Ye Olde System").build();
        SystemAddedEvent event1 = systemAddedEventBuilder.withTimeStamp(Instant.MIN).build();
        SystemAddedEvent event2 = systemAddedEventBuilder.withTimeStamp(Instant.MAX)
                .withSystemCalled("System X").build();
        List<SR_Event> eventLog =
                Stream.of(event1, event2).collect(Collectors.toCollection(ArrayList::new));
        Snapshot expected =
                snapshotBuilder.withSystems(existingSystem, event1.system, event2.system).build();

        Snapshot actual =
                calculator.crunch(snapshotBuilder.withSystem(existingSystem).build(), eventLog);

        assertEquals(expected, actual);
        Assertions.assertEquals(event2.timestamp, actual.timestamp);
    }


    @Test
    public void crunch_existing_snapshot_two_SystemAddedEvents_out_of_order() {
        var calculator = new CurrentStateCalculator();
        SR_System existingSystem = systemBuilder.withName("Ye Olde System").build();
        SystemAddedEvent event1 = systemAddedEventBuilder.withTimeStamp(Instant.MAX).build();
        SystemAddedEvent event2 = systemAddedEventBuilder.withTimeStamp(Instant.MIN)
                .withSystemCalled("System X").build();
        List<SR_Event> eventLog =
                Stream.of(event1, event2).collect(Collectors.toCollection(ArrayList::new));
        Snapshot expected =
                snapshotBuilder.withSystems(existingSystem, event1.system, event2.system).build();

        Snapshot actual =
                calculator.crunch(snapshotBuilder.withSystem(existingSystem).build(), eventLog);

        assertEquals(expected, actual);
        Assertions.assertEquals(event1.timestamp, actual.timestamp);
    }

    @Test
    public void crunch_existing_snapshot_two_SystemAddedEvents_ignores_old_events() {
        var calculator = new CurrentStateCalculator();
        SR_System existingSystem =
                systemBuilder.withLastUpdated(Instant.now()).withName("Ye Olde System").build();
        SystemAddedEvent sneakyOldEvent = systemAddedEventBuilder.withSystemCalled("Too old")
                .withTimeStamp(Instant.MIN).build();
        SystemAddedEvent event2 = systemAddedEventBuilder.withTimeStamp(Instant.MAX)
                .withSystemCalled("System X").build();
        List<SR_Event> eventLog =
                Stream.of(sneakyOldEvent, event2).collect(Collectors.toCollection(ArrayList::new));

        Snapshot actual =
                calculator.crunch(snapshotBuilder.withSystem(existingSystem).build(), eventLog);

        assertEquals(2, actual.systems.size());
        assertEquals(0, actual.systems.stream().filter(s -> s.equals(sneakyOldEvent)).count());
        Assertions.assertEquals(event2.timestamp, actual.timestamp);
    }

    @Test
    public void crunch_existing_snapshot_two_SystemAddedEvents_ignores_events_with_same_timestamp() {
        var calculator = new CurrentStateCalculator();
        Instant sameTime = Instant.now();
        SR_System existingSystem =
                systemBuilder.withLastUpdated(sameTime).withName("Ye Olde System").build();
        SystemAddedEvent sneakyOldEvent =
                systemAddedEventBuilder.withSystemCalled("Too old").withTimeStamp(sameTime).build();
        SystemAddedEvent event2 = systemAddedEventBuilder.withTimeStamp(Instant.MAX)
                .withSystemCalled("System X").build();
        List<SR_Event> eventLog =
                Stream.of(sneakyOldEvent, event2).collect(Collectors.toCollection(ArrayList::new));

        Snapshot actual =
                calculator.crunch(snapshotBuilder.withTimestamp(sameTime).withSystem(existingSystem).build(), eventLog);

        assertEquals(2, actual.systems.size());
        assertEquals(0, actual.systems.stream().filter(s -> s.equals(sneakyOldEvent)).count());
        Assertions.assertEquals(event2.timestamp, actual.timestamp);
    }
}
