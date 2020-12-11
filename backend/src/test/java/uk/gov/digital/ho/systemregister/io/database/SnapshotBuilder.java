package uk.gov.digital.ho.systemregister.io.database;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.model.Snapshot;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SnapshotBuilder {
    SR_SystemBuilder systemBuilder = new SR_SystemBuilder();

    List<SR_System> systems = new ArrayList<>();

    private Instant timestamp = Instant.now();

    public static SnapshotBuilder aSnapshot() {
        return new SnapshotBuilder();
    }

    public Snapshot build() {
        return new Snapshot(systems, timestamp);
    }

    public SnapshotBuilder withASystem() {
        systems.add(systemBuilder.build());
        return this;
    }

    public SnapshotBuilder withSystem(SR_System system) {
        systems.add(system);
        return this;
    }

    public SnapshotBuilder withSystems(SR_System... systems) {
        for (SR_System sys : systems) {
            this.systems.add(sys);
        }
        return this;
    }

    public SnapshotBuilder withTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public SnapshotBuilder withManySystems(int systemCount) {
        for (int i = 0; i < systemCount; i++) {
            systems.add(systemBuilder.withName("System: " + i).build());
        }
        return this;
    }
}
