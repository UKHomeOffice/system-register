package uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.model.Snapshot;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped
public class CurrentSystemRegisterState {
    private final IEventStore eventStore;
    private final CurrentStateCalculator calculator;

    public CurrentSystemRegisterState(@Named("postgres") IEventStore eventStore, CurrentStateCalculator calculator) {
        this.eventStore = eventStore;
        this.calculator = calculator;
    }

    public CurrentState getCurrentState() {
        var latestSnapshot = eventStore.getSnapshot()
                .orElse(Snapshot.empty());
        var eventsSinceSnapshot = eventStore.getEvents(latestSnapshot.timestamp)
                .orElse(List.of());

        return calculator.crunch(
                latestSnapshot,
                eventsSinceSnapshot);
    }
}
