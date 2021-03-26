package uk.gov.digital.ho.systemregister.helpers.builders;

import uk.gov.digital.ho.systemregister.application.messaging.events.PublicFacingUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder;

import java.time.Instant;

import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

public class PublicFacingUpdatedEventBuilder {
    private int id = 1;
    private String publicFacing = "publicfacing";
    private SR_Person author = aPerson().build();
    private Instant timestamp = Instant.now();

    public static PublicFacingUpdatedEventBuilder aPublicFacingUpdatedEvent() {
        return new PublicFacingUpdatedEventBuilder();
    }

    public PublicFacingUpdatedEventBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public PublicFacingUpdatedEventBuilder withPublicFacing(String publicFacing) {
        this.publicFacing = publicFacing;
        return this;
    }

    public PublicFacingUpdatedEventBuilder withAuthor(SR_PersonBuilder author) {
        this.author = author.build();
        return this;
    }

    public PublicFacingUpdatedEventBuilder withTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }


    public PublicFacingUpdatedEvent build() {
        return new PublicFacingUpdatedEvent(id, publicFacing, author, timestamp);
    }

}
