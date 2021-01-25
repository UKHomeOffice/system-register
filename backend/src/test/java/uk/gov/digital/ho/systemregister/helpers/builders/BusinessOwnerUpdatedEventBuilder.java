package uk.gov.digital.ho.systemregister.helpers.builders;

import uk.gov.digital.ho.systemregister.application.messaging.events.BusinessOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder;

import java.time.Instant;

import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

public class BusinessOwnerUpdatedEventBuilder {
    private int id = 1;
    private String businessOwner = "Barry Blue";
    private SR_Person author = aPerson().build();
    private Instant timestamp = Instant.now();

    public static BusinessOwnerUpdatedEventBuilder aBusinessOwnerUpdatedEvent() {
        return new BusinessOwnerUpdatedEventBuilder();
    }

    public BusinessOwnerUpdatedEventBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public BusinessOwnerUpdatedEventBuilder withBusinessOwner(String businessOwner) {
        this.businessOwner = businessOwner;
        return this;
    }

    public BusinessOwnerUpdatedEventBuilder withAuthor(SR_PersonBuilder author) {
        this.author = author.build();
        return this;
    }

    public BusinessOwnerUpdatedEventBuilder withTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public BusinessOwnerUpdatedEvent build() {
        return new BusinessOwnerUpdatedEvent(id, businessOwner, author, timestamp);
    }
}
