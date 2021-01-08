package uk.gov.digital.ho.systemregister.helpers.builders;

import uk.gov.digital.ho.systemregister.application.messaging.events.SystemDescriptionUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder;

import java.time.Instant;

import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

public final class SystemDescriptionUpdatedEventBuilder {
    public int id = 4;
    public String description = "an interesting description";
    public SR_Person author = aPerson().build();
    public Instant timestamp = Instant.now();

    public static SystemDescriptionUpdatedEventBuilder aSystemDescriptionUpdatedEvent() {
        return new SystemDescriptionUpdatedEventBuilder();
    }

    public SystemDescriptionUpdatedEventBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public SystemDescriptionUpdatedEventBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public SystemDescriptionUpdatedEventBuilder withAuthor(SR_PersonBuilder author) {
        this.author = author.build();
        return this;
    }

    public SystemDescriptionUpdatedEventBuilder withTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public SystemDescriptionUpdatedEvent build() {
        return new SystemDescriptionUpdatedEvent(id, description, author, timestamp);
    }

    private SystemDescriptionUpdatedEventBuilder() {
    }
}
