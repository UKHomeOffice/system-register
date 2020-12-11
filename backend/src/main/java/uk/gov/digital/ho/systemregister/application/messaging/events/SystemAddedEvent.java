package uk.gov.digital.ho.systemregister.application.messaging.events;

import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import java.beans.ConstructorProperties;
import java.time.Instant;
import java.util.Objects;

public class SystemAddedEvent extends SR_SystemEvent {
    public SR_System system;

    @ConstructorProperties({"system", "author"})
    public SystemAddedEvent(SR_System system, SR_Person author) {
        this(system, author, Instant.now());
    }

    public SystemAddedEvent(SR_System system, SR_Person author, Instant timestamp) {
        super(author, timestamp);
        this.system = system;
    }

    @Override
    public int getSystemId() {
        return system.id;
    }

    @Override
    public Instant getUpdateTimestamp() {
        return system.lastUpdated;
    }

    @Override
    public SR_System update(SR_System system) {
        if (system != null) {
            throw new IllegalStateException(String.format("system '%s' already exists", system.name));
        }
        return this.system;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SystemAddedEvent)) {
            return false;
        }
        SystemAddedEvent systemAddedEvent = (SystemAddedEvent) o;
        return Objects.equals(system, systemAddedEvent.system);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(system);
    }

    @Override
    public String toString() {
        return "{" +
                " system='" + system + "'" +
                "}";
    }
}
