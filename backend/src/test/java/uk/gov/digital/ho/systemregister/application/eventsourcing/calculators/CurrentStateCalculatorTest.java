package uk.gov.digital.ho.systemregister.application.eventsourcing.calculators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.model.Snapshot;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemUpdater;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.helpers.builders.CriticalityUpdatedEventBuilder.aCriticalityUpdatedEvent;
import static uk.gov.digital.ho.systemregister.helpers.builders.ProductOwnerUpdatedEventBuilder.aProductOwnerUpdatedEvent;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;
import static uk.gov.digital.ho.systemregister.helpers.builders.SystemAddedEventBuilder.aSystemAddedEvent;
import static uk.gov.digital.ho.systemregister.io.database.SnapshotBuilder.aSnapshot;

public class CurrentStateCalculatorTest {
    private CurrentStateCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new CurrentStateCalculator();
    }

    @Test
    public void stateDoesNotChangeIfThereAreNoEvents() {
        var systemTimestamp = Instant.now();
        var snapshotTimestamp = Instant.now().minusSeconds(1);
        var system = aSystem().withLastUpdated(systemTimestamp).build();
        var snapshot = aSnapshot()
                .withSystem(system)
                .withTimestamp(snapshotTimestamp)
                .build();

        var state = calculator.crunch(snapshot, List.of());

        assertThat(state).usingRecursiveComparison()
                .isEqualTo(new CurrentState(
                        Map.of(system, new UpdateMetadata(null, systemTimestamp)),
                        snapshotTimestamp));
    }

    @Test
    public void addedSystemIsOnlyMemberIfSnapshotIsEmpty() {
        var systemTimestamp = Instant.now();
        var system = aSystem().withLastUpdated(systemTimestamp);
        var event = aSystemAddedEvent()
                .withSystem(system)
                .withAuthor(aPerson())
                .build();

        var state = calculator.crunch(Snapshot.empty(), List.of(event));

        assertThat(state).usingRecursiveComparison()
                .isEqualTo(new CurrentState(
                        Map.of(event.system, new UpdateMetadata(event.author, event.system.lastUpdated)),
                        event.timestamp));
    }

    @Test
    void canAddDifferentSystemWhenSnapshotContainsExistingSystems() {
        var existingSystem = aSystem().withName("existing system").build();
        var snapshot = aSnapshot().withSystem(existingSystem).build();
        var newSystem = aSystem().withName("new system");
        var event = aSystemAddedEvent()
                .withSystem(newSystem)
                .withAuthor(aPerson())
                .build();

        var state = calculator.crunch(
                snapshot,
                List.of(event));

        assertThat(state).usingRecursiveComparison()
                .isEqualTo(new CurrentState(
                        Map.of(
                                existingSystem, new UpdateMetadata(null, existingSystem.lastUpdated),
                                event.system, new UpdateMetadata(event.author, event.system.lastUpdated)),
                        event.timestamp));
    }

    @Test
    void stateUsesLatestEventTimestamp() {
        var snapshotSystem = aSystem().withName("in snapshot").build();
        var snapshot = aSnapshot().withSystem(snapshotSystem).build();
        var earlyEvent = aSystemAddedEvent()
                .withSystem(aSystem().withName("early"))
                .withTimeStamp(Instant.now())
                .build();
        var lateEvent = aSystemAddedEvent()
                .withSystem(aSystem().withName("late"))
                .withTimeStamp(Instant.MAX)
                .build();

        var state = calculator.crunch(snapshot, List.of(earlyEvent, lateEvent));

        assertThat(state).usingRecursiveComparison()
                .isEqualTo(new CurrentState(
                        Map.of(
                                snapshotSystem, new UpdateMetadata(null, snapshotSystem.lastUpdated),
                                earlyEvent.system, new UpdateMetadata(earlyEvent.author, earlyEvent.system.lastUpdated),
                                lateEvent.system, new UpdateMetadata(lateEvent.author, lateEvent.system.lastUpdated)),
                        lateEvent.timestamp));
    }

    @Test
    void stateUsesLatestEventTimestampRegardlessOfOrder() {
        var snapshotSystem = aSystem().withName("in snapshot").build();
        var snapshot = aSnapshot().withSystem(snapshotSystem).build();
        var earlyEvent = aSystemAddedEvent()
                .withSystem(aSystem().withName("early"))
                .withTimeStamp(Instant.now())
                .build();
        var lateEvent = aSystemAddedEvent()
                .withSystem(aSystem().withName("late"))
                .withTimeStamp(Instant.MAX)
                .build();

        var state = calculator.crunch(snapshot, List.of(lateEvent, earlyEvent));

        assertThat(state).usingRecursiveComparison()
                .isEqualTo(new CurrentState(
                        Map.of(
                                snapshotSystem, new UpdateMetadata(null, snapshotSystem.lastUpdated),
                                earlyEvent.system, new UpdateMetadata(earlyEvent.author, earlyEvent.system.lastUpdated),
                                lateEvent.system, new UpdateMetadata(lateEvent.author, lateEvent.system.lastUpdated)),
                        lateEvent.timestamp));
    }

    @Test
    void ignoresOldEvents() {
        var snapshotSystem = aSystem().withName("in snapshot").build();
        var snapshot = aSnapshot().withSystem(snapshotSystem).build();
        var oldEvent = aSystemAddedEvent()
                .withSystem(aSystem().withName("early"))
                .withTimeStamp(Instant.MIN)
                .build();
        var contemporaneousEvent = aSystemAddedEvent()
                .withSystem(aSystem().withName("same time"))
                .withTimeStamp(snapshot.timestamp)
                .build();

        var state = calculator.crunch(snapshot, List.of(oldEvent, contemporaneousEvent));

        assertThat(state).usingRecursiveComparison()
                .isEqualTo(new CurrentState(
                        Map.of(snapshotSystem, new UpdateMetadata(null, snapshotSystem.lastUpdated)),
                        snapshot.timestamp));
    }

    @Test
    void canApplyUpdateCriticalityEvent() {
        var snapshot = aSnapshot().build();
        var templateSystem = aSystem().withId(123);
        var initialEvent = aSystemAddedEvent()
                .withId(123)
                .withSystem(templateSystem.withCriticality("low"))
                .build();
        var author = aPerson().withUsername("editor");
        var criticalityUpdatedEvent = aCriticalityUpdatedEvent()
                .withId(123)
                .withCriticality("high")
                .withAuthor(author)
                .build();
        var expectedSystem = templateSystem
                .withCriticality("high")
                .build();

        var updatedState = calculator.crunch(snapshot, List.of(initialEvent, criticalityUpdatedEvent));

        assertThat(updatedState).usingRecursiveComparison()
                .isEqualTo(new CurrentState(
                        Map.of(expectedSystem, new UpdateMetadata(author.build(), criticalityUpdatedEvent.timestamp)),
                        criticalityUpdatedEvent.timestamp));
    }

    @Test
    void combinesDifferentEventTypesToChangeSystems() {
        var snapshot = aSnapshot().build();
        var templateSystem = aSystem().withId(123);
        var initialEvent = aSystemAddedEvent()
                .withId(123)
                .withSystem(templateSystem.withProductOwner("initial owner"))
                .build();
        var productOwnerAuthor = aPerson().withUsername("editor");
        var productOwnerUpdatedEvent = aProductOwnerUpdatedEvent()
                .withId(123)
                .withProductOwner("new owner")
                .withAuthor(productOwnerAuthor)
                .build();
        var expectedSystem = templateSystem
                .withProductOwner("new owner")
                .build();

        var updatedState = calculator.crunch(snapshot, List.of(initialEvent, productOwnerUpdatedEvent));

        assertThat(updatedState).usingRecursiveComparison()
                .isEqualTo(new CurrentState(
                        Map.of(expectedSystem, new UpdateMetadata(productOwnerAuthor.build(), productOwnerUpdatedEvent.timestamp)),
                        productOwnerUpdatedEvent.timestamp));
    }

    @Test
    void updatesSystemWithNewState() {
        var updatedSystem = aSystem().build();
        var updater = mock(SystemUpdater.class);
        when(updater.update(any())).thenReturn(updatedSystem);

        SR_System system = calculator.applyUpdateToSystem(aSystem().build(), updater);

        assertThat(system).usingRecursiveComparison()
                .isEqualTo(updatedSystem);
    }
}
