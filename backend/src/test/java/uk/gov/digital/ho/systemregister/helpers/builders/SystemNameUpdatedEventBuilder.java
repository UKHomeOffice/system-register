package uk.gov.digital.ho.systemregister.helpers.builders;

import uk.gov.digital.ho.systemregister.application.messaging.events.SystemNameUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder;

import java.time.Instant;

import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

public class SystemNameUpdatedEventBuilder {
    private int id = 1;
    private String systemName = "System 1";
    private SR_Person author = aPerson().build();
    private Instant timestamp = Instant.now();

    public static SystemNameUpdatedEventBuilder aSystemNameUpdatedEvent() {
        return new SystemNameUpdatedEventBuilder();
    }

    public SystemNameUpdatedEventBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public SystemNameUpdatedEventBuilder withSystemName(String systemName) {
        this.systemName = systemName;
        return this;
    }

    public SystemNameUpdatedEventBuilder withAuthor(SR_PersonBuilder author) {
        this.author = author.build();
        return this;
    }

    public SystemNameUpdatedEventBuilder withTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }


    public SystemNameUpdatedEvent build() {
        return new SystemNameUpdatedEvent(id, systemName, author, timestamp);
    }

}
