package uk.gov.digital.ho.systemregister.test.helpers.builders;

import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAddedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import java.time.Instant;

public class SystemAddedEventBuilder {
    private Instant timeStamp = Instant.now();
    private final SR_Person author = new SR_Person("Corey logan");
    private final SR_SystemBuilder systemBuilder = new SR_SystemBuilder();

    public SystemAddedEvent build() {
        var evt = new SystemAddedEvent(systemBuilder.build(), author);
        evt.timestamp = this.timeStamp;
        return evt;
    }

    public SystemAddedEventBuilder withId(int id) {
        systemBuilder.withId(id);
        return this;
    }

    public SystemAddedEventBuilder withSystemCalled(String systemName) {
        systemBuilder.withName(systemName);
        return this;
    }

    public SystemAddedEventBuilder withLastUpdated(Instant timestamp) {
        systemBuilder.withLastUpdated(timestamp);
        return this;
    }

    public SystemAddedEventBuilder withTimeStamp(Instant time) {
        this.timeStamp = time;
        return this;
    }
}
