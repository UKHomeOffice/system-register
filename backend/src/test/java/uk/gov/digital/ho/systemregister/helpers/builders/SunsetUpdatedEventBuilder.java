package uk.gov.digital.ho.systemregister.helpers.builders;

import uk.gov.digital.ho.systemregister.application.messaging.events.SunsetUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder;
import uk.gov.digital.ho.systemregister.domain.SR_Sunset;
import uk.gov.digital.ho.systemregister.domain.SR_SunsetBuilder;

import java.time.Instant;

import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.domain.SR_SunsetBuilder.aSunset;

public class SunsetUpdatedEventBuilder {
    private int id = 1;
    private SR_Sunset sunset = aSunset().build();
    private SR_Person author = aPerson().build();
    private Instant timestamp = Instant.now();

    public static SunsetUpdatedEventBuilder aSunsetUpdatedEvent() {
        return new SunsetUpdatedEventBuilder();
    }

    public SunsetUpdatedEventBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public SunsetUpdatedEventBuilder withSunset(SR_SunsetBuilder sunset) {
        this.sunset = sunset.build();
        return this;
    }

    public SunsetUpdatedEventBuilder withAuthor(SR_PersonBuilder author) {
        this.author = author.build();
        return this;
    }

    public SunsetUpdatedEventBuilder withTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public SunsetUpdatedEvent build() {
        return new SunsetUpdatedEvent(id, sunset, author, timestamp);
    }
}
