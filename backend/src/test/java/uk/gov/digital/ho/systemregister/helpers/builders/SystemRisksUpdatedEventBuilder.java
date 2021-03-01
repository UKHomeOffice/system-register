package uk.gov.digital.ho.systemregister.helpers.builders;

import uk.gov.digital.ho.systemregister.application.messaging.events.SystemRisksUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder;
import uk.gov.digital.ho.systemregister.domain.SR_Risk;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

public final class SystemRisksUpdatedEventBuilder {
    public int id = 4;
    public List<SR_Risk> risks = List.of(new SR_Risk("some risk", "low", "some reason"));
    public SR_Person author = aPerson().build();
    public Instant timestamp = Instant.now();

    public static SystemRisksUpdatedEventBuilder aSystemRisksUpdatedEvent() {
        return new SystemRisksUpdatedEventBuilder();
    }

    public SystemRisksUpdatedEventBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public SystemRisksUpdatedEventBuilder withRisks(List<SR_Risk> risks) {
        this.risks = risks;
        return this;
    }

    public SystemRisksUpdatedEventBuilder withAuthor(SR_PersonBuilder author) {
        this.author = author.build();
        return this;
    }

    public SystemRisksUpdatedEventBuilder withTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public SystemRisksUpdatedEvent build() {
        return new SystemRisksUpdatedEvent(id, risks, author, timestamp);
    }

    private SystemRisksUpdatedEventBuilder() {
    }
}
