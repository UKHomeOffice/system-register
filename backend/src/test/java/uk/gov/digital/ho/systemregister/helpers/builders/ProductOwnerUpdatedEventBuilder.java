package uk.gov.digital.ho.systemregister.helpers.builders;

import uk.gov.digital.ho.systemregister.application.messaging.events.ProductOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder;

import java.time.Instant;

import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

public class ProductOwnerUpdatedEventBuilder {
    private int id = 1;
    private String productOwner = "Barry White";
    private SR_Person author = aPerson().build();
    private Instant timestamp = Instant.now();

    public static ProductOwnerUpdatedEventBuilder aProductOwnerUpdatedEvent() {
        return new ProductOwnerUpdatedEventBuilder();
    }

    public ProductOwnerUpdatedEventBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public ProductOwnerUpdatedEventBuilder withProductOwner(String productOwner) {
        this.productOwner = productOwner;
        return this;
    }

    public ProductOwnerUpdatedEventBuilder withAuthor(SR_PersonBuilder author) {
        this.author = author.build();
        return this;
    }

    public ProductOwnerUpdatedEventBuilder withTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }


    public ProductOwnerUpdatedEvent build() {
        return new ProductOwnerUpdatedEvent(id, productOwner, author, timestamp);
    }

}
