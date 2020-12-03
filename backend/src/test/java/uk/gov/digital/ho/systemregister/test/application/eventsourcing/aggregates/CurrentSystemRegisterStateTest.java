package uk.gov.digital.ho.systemregister.test.application.eventsourcing.aggregates;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentState;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;
import uk.gov.digital.ho.systemregister.test.helpers.FakeEventStore;

import java.util.Map;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.digital.ho.systemregister.test.helpers.builders.SystemAddedEventBuilder.aSystemAddedEvent;

public class CurrentSystemRegisterStateTest {
    private final IEventStore fakeEventStore = new FakeEventStore();
    private CurrentSystemRegisterState systemRegisterState;

    @BeforeEach
    void setUp() {
        systemRegisterState = new CurrentSystemRegisterState(fakeEventStore);
    }

    @Test
    public void no_snapshot_no_events() {
        var state = systemRegisterState.getSystems();

        assertThat(state).usingRecursiveComparison()
                .isEqualTo(new CurrentState(Map.of(), EPOCH));
    }

    @Test
    public void no_snapshot_one_system_added_event() {
        var event = aSystemAddedEvent().build();
        fakeEventStore.save(event);

        var state = systemRegisterState.getSystems();

        assertThat(state.getSystems())
                .containsExactly(event.system);
    }
}
