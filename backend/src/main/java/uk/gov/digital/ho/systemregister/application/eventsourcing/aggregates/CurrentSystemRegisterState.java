package uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.model.Snapshot;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import java.util.List;

public class CurrentSystemRegisterState {
    private final IEventStore eventStore;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public CurrentSystemRegisterState(IEventStore eventStore) {
        this.eventStore = eventStore;
    }

    public CurrentState getSystems() {
        var calculator = new CurrentStateCalculator();
        var latestSnapshot = eventStore.getSnapshot()
                .orElse(Snapshot.empty());
        var eventsSinceSnapshot = eventStore.getEvents(latestSnapshot.timestamp)
                .orElse(List.of());

        return calculator.crunch2(
                latestSnapshot,
                eventsSinceSnapshot);
    }
}
