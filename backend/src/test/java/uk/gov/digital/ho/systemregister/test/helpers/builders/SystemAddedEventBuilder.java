package uk.gov.digital.ho.systemregister.test.helpers.builders;

import java.time.Instant;

import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAddedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

public class SystemAddedEventBuilder {
    private Instant timeStamp = Instant.now();
    private SR_Person author = new SR_Person("Corey logan");
    private SR_SystemBuilder systemBuilder = new SR_SystemBuilder();

    public SystemAddedEvent build() {
        var evt = new SystemAddedEvent(systemBuilder.build(), author);
        evt.timestamp = this.timeStamp;
        return evt;
    }

    public SystemAddedEventBuilder withSystemCalled(String systemName) {
        systemBuilder.withName(systemName);
        return this;
    }

    public SystemAddedEventBuilder withTimeStamp(Instant time) {
        this.timeStamp = time;
        return this;
    }

}
