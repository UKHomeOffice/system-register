package uk.gov.digital.ho.systemregister.helpers.builders;

import uk.gov.digital.ho.systemregister.application.messaging.events.TechnicalOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder;

import java.time.Instant;

import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

public class TechnicalOwnerUpdatedEventBuilder {
    private int id = 1;
    private String technicalOwner = "Barry Blue";
    private SR_Person author = aPerson().build();
    private Instant timestamp = Instant.now();

    public static TechnicalOwnerUpdatedEventBuilder aTechnicalOwnerUpdatedEvent() {
        return new TechnicalOwnerUpdatedEventBuilder();
    }

    public TechnicalOwnerUpdatedEventBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public TechnicalOwnerUpdatedEventBuilder withTechnicalOwner(String technicalOwner) {
        this.technicalOwner = technicalOwner;
        return this;
    }

    public TechnicalOwnerUpdatedEventBuilder withAuthor(SR_PersonBuilder author) {
        this.author = author.build();
        return this;
    }

    public TechnicalOwnerUpdatedEventBuilder withTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public TechnicalOwnerUpdatedEvent build() {
        return new TechnicalOwnerUpdatedEvent(id, technicalOwner, author, timestamp);
    }
}
