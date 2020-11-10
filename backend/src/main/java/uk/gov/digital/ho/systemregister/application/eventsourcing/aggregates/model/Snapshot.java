package uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.model;

import java.time.Instant;
import java.util.*;

import uk.gov.digital.ho.systemregister.domain.SR_System;

public class Snapshot { //todo I want to make all these immutable but cant because they need to be serialisable, help

    public List<SR_System> systems;
    public Instant timestamp;

    public Snapshot() {
    }

    public Snapshot(List<SR_System> systems, Instant timestamp) {
        this.systems = systems;
        this.timestamp = timestamp;
    }

    public static Snapshot empty() {
        return new Snapshot(new ArrayList<>(), Instant.ofEpochSecond(0));
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Snapshot)) {
            return false;
        }
        Snapshot snapshot = (Snapshot) o;
        return Objects.equals(systems, snapshot.systems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(systems);
    }
}
