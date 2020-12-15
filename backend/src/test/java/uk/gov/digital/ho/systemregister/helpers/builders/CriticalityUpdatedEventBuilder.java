package uk.gov.digital.ho.systemregister.helpers.builders;

import uk.gov.digital.ho.systemregister.application.messaging.events.CriticalityUpdatedEvent;
import uk.gov.digital.ho.systemregister.application.messaging.events.ProductOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder;

import java.time.Instant;

import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

public class CriticalityUpdatedEventBuilder {
    private int id = 1;
    private String criticality = "unknown";
    private SR_Person author = aPerson().build();
    private Instant timestamp = Instant.now();

    public static CriticalityUpdatedEventBuilder aCriticalityUpdatedEvent() {
        return new CriticalityUpdatedEventBuilder();
    }

    public CriticalityUpdatedEventBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public CriticalityUpdatedEventBuilder withCriticality(String criticality) {
        this.criticality = criticality;
        return this;
    }

    public CriticalityUpdatedEventBuilder withAuthor(SR_PersonBuilder author) {
        this.author = author.build();
        return this;
    }

    public CriticalityUpdatedEventBuilder withTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }


    public CriticalityUpdatedEvent build() {
        return new CriticalityUpdatedEvent(author, timestamp, id, criticality);
    }

}
