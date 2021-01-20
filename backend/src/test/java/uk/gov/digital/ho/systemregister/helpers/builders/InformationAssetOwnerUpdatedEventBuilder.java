package uk.gov.digital.ho.systemregister.helpers.builders;

import uk.gov.digital.ho.systemregister.application.messaging.events.InformationAssetOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder;

import java.time.Instant;

import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

public class InformationAssetOwnerUpdatedEventBuilder {
    private int id = 1;
    private String informationAssetOwner = "Barry Baloon";
    private SR_Person author = aPerson().build();
    private Instant timestamp = Instant.now();

    public static InformationAssetOwnerUpdatedEventBuilder anInformationAssetOwnerUpdatedEvent() {
        return new InformationAssetOwnerUpdatedEventBuilder();
    }

    public InformationAssetOwnerUpdatedEventBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public InformationAssetOwnerUpdatedEventBuilder withInformationAssetOwner(String informationAssetOwner) {
        this.informationAssetOwner = informationAssetOwner;
        return this;
    }

    public InformationAssetOwnerUpdatedEventBuilder withAuthor(SR_PersonBuilder author) {
        this.author = author.build();
        return this;
    }

    public InformationAssetOwnerUpdatedEventBuilder withTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }


    public InformationAssetOwnerUpdatedEvent build() {
        return new InformationAssetOwnerUpdatedEvent(id, informationAssetOwner, author, timestamp);
    }

}
