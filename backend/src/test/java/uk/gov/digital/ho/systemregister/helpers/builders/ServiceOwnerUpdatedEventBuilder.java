package uk.gov.digital.ho.systemregister.helpers.builders;

import uk.gov.digital.ho.systemregister.application.messaging.events.ServiceOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.application.messaging.events.TechnicalOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder;

import java.time.Instant;

import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

public class ServiceOwnerUpdatedEventBuilder {
    private int id = 1;
    private String serviceOwner = "Josh Lowe";
    private SR_Person author = aPerson().build();
    private Instant timestamp = Instant.now();

    public static ServiceOwnerUpdatedEventBuilder aServiceOwnerUpdatedEvent() {
        return new ServiceOwnerUpdatedEventBuilder();
    }

    public ServiceOwnerUpdatedEventBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public ServiceOwnerUpdatedEventBuilder withServiceOwner(String serviceOwner) {
        this.serviceOwner = serviceOwner;
        return this;
    }

    public ServiceOwnerUpdatedEventBuilder withAuthor(SR_PersonBuilder author) {
        this.author = author.build();
        return this;
    }

    public ServiceOwnerUpdatedEventBuilder withTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public ServiceOwnerUpdatedEvent build() {
        return new ServiceOwnerUpdatedEvent(id, serviceOwner, author, timestamp);
    }
}
