package uk.gov.digital.ho.systemregister.helpers.builders;

import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAliasesUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder;

import java.time.Instant;
import java.util.Set;

import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

public final class SystemAliasesUpdatedEventBuilder {
    public int id = 4;
    public Set<String> aliases = Set.of("aka");
    public SR_Person author = aPerson().build();
    public Instant timestamp = Instant.now();

    public static SystemAliasesUpdatedEventBuilder aSystemAliasesUpdatedEvent() {
        return new SystemAliasesUpdatedEventBuilder();
    }

    public SystemAliasesUpdatedEventBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public SystemAliasesUpdatedEventBuilder withAliases(Set<String> aliases) {
        this.aliases = aliases;
        return this;
    }

    public SystemAliasesUpdatedEventBuilder withAuthor(SR_PersonBuilder author) {
        this.author = author.build();
        return this;
    }

    public SystemAliasesUpdatedEventBuilder withTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public SystemAliasesUpdatedEvent build() {
        return new SystemAliasesUpdatedEvent(id, aliases, author, timestamp);
    }

    private SystemAliasesUpdatedEventBuilder() {
    }
}
