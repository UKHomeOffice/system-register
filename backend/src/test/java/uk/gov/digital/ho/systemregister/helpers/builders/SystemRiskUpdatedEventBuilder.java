package uk.gov.digital.ho.systemregister.helpers.builders;

import uk.gov.digital.ho.systemregister.application.messaging.events.SystemRiskUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder;
import uk.gov.digital.ho.systemregister.domain.SR_Risk;

import java.time.Instant;

import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

public final class SystemRiskUpdatedEventBuilder {
    public int id = 4;
    public SR_Risk risk = new SR_Risk("some risk", "low", "some reason");
    public SR_Person author = aPerson().build();
    public Instant timestamp = Instant.now();

    public static SystemRiskUpdatedEventBuilder aSystemRiskUpdatedEvent() {
        return new SystemRiskUpdatedEventBuilder();
    }

    public SystemRiskUpdatedEventBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public SystemRiskUpdatedEventBuilder withRisk(SR_Risk risk) {
        this.risk = risk;
        return this;
    }

    public SystemRiskUpdatedEventBuilder withAuthor(SR_PersonBuilder author) {
        this.author = author.build();
        return this;
    }

    public SystemRiskUpdatedEventBuilder withTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public SystemRiskUpdatedEvent build() {
        return new SystemRiskUpdatedEvent(id, risk, author, timestamp);
    }

    private SystemRiskUpdatedEventBuilder() {
    }
}
