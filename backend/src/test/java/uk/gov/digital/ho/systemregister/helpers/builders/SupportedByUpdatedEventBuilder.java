package uk.gov.digital.ho.systemregister.helpers.builders;

import uk.gov.digital.ho.systemregister.application.messaging.events.SupportedByUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder;

import java.time.Instant;

import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

public class SupportedByUpdatedEventBuilder {
    private int id = 1;
    private String supportedBy = "Jo Smith";
    private SR_Person author = aPerson().build();
    private Instant timestamp = Instant.now();

    public static SupportedByUpdatedEventBuilder aSupportedByUpdatedEvent() {
        return new SupportedByUpdatedEventBuilder();
    }

    public SupportedByUpdatedEventBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public SupportedByUpdatedEventBuilder withSupportedBy(String supportedBy) {
        this.supportedBy = supportedBy;
        return this;
    }

    public SupportedByUpdatedEventBuilder withAuthor(SR_PersonBuilder author) {
        this.author = author.build();
        return this;
    }

    public SupportedByUpdatedEventBuilder withTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public SupportedByUpdatedEvent build() {
        return new SupportedByUpdatedEvent(id, supportedBy, author, timestamp);
    }
}
