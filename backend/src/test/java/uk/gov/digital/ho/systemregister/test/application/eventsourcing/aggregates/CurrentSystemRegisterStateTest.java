package uk.gov.digital.ho.systemregister.test.application.eventsourcing.aggregates;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.test.helpers.FakeEventStore;
import uk.gov.digital.ho.systemregister.test.helpers.builders.SystemAddedEventBuilder;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.model.Snapshot;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAddedEvent;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CurrentSystemRegisterStateTest {
    IEventStore fakeEventStore = new FakeEventStore();

    @Test
    public void no_snapshot_no_events() {
        var sut = new CurrentSystemRegisterState(fakeEventStore);

        var actual = sut.getSystems();

        Assertions.assertEquals(Snapshot.empty(), actual);
    }

    @Test
    public void no_snapshot_one_system_added_event() {
        SystemAddedEvent evt = new SystemAddedEventBuilder().build();
        fakeEventStore.save(evt);
        var sut = new CurrentSystemRegisterState(fakeEventStore);

        var actual = sut.getSystems();

        assertEquals(1, actual.systems.size());
        assertEquals(evt.system.name, actual.systems.get(0).name);
    }
}
