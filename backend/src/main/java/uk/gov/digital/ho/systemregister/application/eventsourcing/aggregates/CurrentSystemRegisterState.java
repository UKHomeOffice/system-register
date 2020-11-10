package uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates;

import org.jboss.logging.Logger;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.model.Snapshot;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

public class CurrentSystemRegisterState {
    private static final Logger LOG = Logger.getLogger(CurrentSystemRegisterState.class);

    IEventStore eventStore;

    public CurrentSystemRegisterState(IEventStore eventStore) {
        this.eventStore = eventStore;
    }

    public Snapshot getSystems() {
        var latestSnapshot = eventStore.getSnapshot().orElse(Snapshot.empty());
        var eventsSinceSnapshot = eventStore.getEvents(latestSnapshot.timestamp);
        if (eventsSinceSnapshot.isEmpty()) {
            return latestSnapshot;
        }
        var snapshot = new CurrentStateCalculator().crunch(latestSnapshot, eventsSinceSnapshot.get());
        return snapshot;
    }

}
