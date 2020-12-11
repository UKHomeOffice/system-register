package uk.gov.digital.ho.systemregister.application.messaging.events;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.gov.digital.ho.systemregister.test.helpers.builders.SR_SystemBuilder.aSystem;
import static uk.gov.digital.ho.systemregister.test.helpers.builders.SystemAddedEventBuilder.aSystemAddedEvent;

class SystemAddedEventTest {
    @Test
    void updatedTimestampIsDerivedFromTheSystem() {
        var systemTimestamp = Instant.now();
        var eventTimestamp = Instant.now().plus(Duration.ofSeconds(5));
        var event = aSystemAddedEvent()
                .withTimeStamp(eventTimestamp)
                .withSystem(aSystem()
                        .withLastUpdated(systemTimestamp))
                .build();

        var updateTimestamp = event.getUpdateTimestamp();

        assertThat(updateTimestamp).isSameAs(systemTimestamp);
    }

    @Test
    void returnsTheAddedSystem() {
        var system = aSystem();
        var event = aSystemAddedEvent()
                .withSystem(system)
                .build();

        var updatedSystem = event.update(null);

        assertThat(updatedSystem).usingRecursiveComparison()
                .isEqualTo(system.build());
    }

    @Test
    void raisesAnExceptionIfTheEventUpdatesAnExistingSystem() {
        var existingSystem = aSystem().build();
        var event = aSystemAddedEvent().build();

        assertThatThrownBy(() -> event.update(existingSystem))
                .isInstanceOf(IllegalStateException.class);
    }
}
