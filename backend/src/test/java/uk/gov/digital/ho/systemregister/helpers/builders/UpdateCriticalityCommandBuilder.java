package uk.gov.digital.ho.systemregister.helpers.builders;

import uk.gov.digital.ho.systemregister.application.messaging.events.CriticalityUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder;

import java.time.Instant;

import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

public class UpdateCriticalityCommandBuilder {
    private int id = 1;
    private String criticality = "unknown";
    private SR_Person author = aPerson().build();
    private Instant timestamp = Instant.now();

    public static UpdateCriticalityCommandBuilder aCriticalityUpdatedEvent() {
        return new UpdateCriticalityCommandBuilder();
    }

    public UpdateCriticalityCommandBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public UpdateCriticalityCommandBuilder withCriticality(String criticality) {
        this.criticality = criticality;
        return this;
    }

    public UpdateCriticalityCommandBuilder withAuthor(SR_PersonBuilder author) {
        this.author = author.build();
        return this;
    }

    public UpdateCriticalityCommandBuilder withTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public CriticalityUpdatedEvent build() {
        return new CriticalityUpdatedEvent(author, timestamp, id, criticality);
    }

}