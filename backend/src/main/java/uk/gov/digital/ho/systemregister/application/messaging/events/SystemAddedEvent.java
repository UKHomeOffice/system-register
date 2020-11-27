package uk.gov.digital.ho.systemregister.application.messaging.events;

import java.beans.ConstructorProperties;
import java.time.Instant;
import java.util.Objects;

import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import javax.json.bind.annotation.JsonbCreator;

public class SystemAddedEvent extends SR_Event {
    public SR_System system;

    public SystemAddedEvent() {
    }

    @ConstructorProperties({"system", "author"})
    public SystemAddedEvent(SR_System system, SR_Person author) {
        this.timestamp = Instant.now();
        this.author = author;
        this.system = system;
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
