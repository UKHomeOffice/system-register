package uk.gov.digital.ho.systemregister.helpers.builders;

import uk.gov.digital.ho.systemregister.application.messaging.events.DevelopedByUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder;

import java.time.Instant;

import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

public class DevelopedByUpdatedEventBuilder {
    private int id = 1;
    private String developedBy = "Katey Keyboard";
    private SR_Person author = aPerson().build();
    private Instant timestamp = Instant.now();

    public static DevelopedByUpdatedEventBuilder aDevelopedByUpdatedEvent() {
        return new DevelopedByUpdatedEventBuilder();
    }

    public DevelopedByUpdatedEventBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public DevelopedByUpdatedEventBuilder withDevelopedBy(String developedBy) {
        this.developedBy = developedBy;
        return this;
    }

    public DevelopedByUpdatedEventBuilder withAuthor(SR_PersonBuilder author) {
        this.author = author.build();
        return this;
    }

    public DevelopedByUpdatedEventBuilder withTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }


    public DevelopedByUpdatedEvent build() {
        return new DevelopedByUpdatedEvent(id, developedBy, author, timestamp);
    }

}
