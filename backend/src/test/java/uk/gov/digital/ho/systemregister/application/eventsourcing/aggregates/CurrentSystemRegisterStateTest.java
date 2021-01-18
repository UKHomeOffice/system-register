package uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.helpers.FakeEventStore;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import java.util.Map;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.digital.ho.systemregister.helpers.builders.SystemAddedEventBuilder.aSystemAddedEvent;

public class CurrentSystemRegisterStateTest {
    private final IEventStore fakeEventStore = new FakeEventStore();
    private CurrentSystemRegisterState systemRegisterState;

    @BeforeEach
    void setUp() {
        systemRegisterState = new CurrentSystemRegisterState(fakeEventStore, new CurrentStateCalculator());
    }

    @Test
    public void no_snapshot_no_events() {
        var state = systemRegisterState.getCurrentState();

        assertThat(state).usingRecursiveComparison()
                .isEqualTo(new CurrentState(Map.of(), EPOCH));
    }

    @Test
    public void no_snapshot_one_system_added_event() {
        var event = aSystemAddedEvent().build();
        fakeEventStore.save(event);

        var state = systemRegisterState.getCurrentState();

        assertThat(state.getSystems())
                .containsExactly(event.system);
    }
}
