package uk.gov.digital.ho.systemregister.application.messaging.eventhandlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.events.SunsetUpdatedEvent;
import uk.gov.digital.ho.systemregister.application.messaging.events.SupportedByUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_SunsetBuilder;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import java.time.Instant;
import java.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.domain.SR_SunsetBuilder.aSunset;

class SunsetUpdatedEventHandlerTest {
    private final IEventStore store = mock(IEventStore.class);

    private SunsetUpdatedEventHandler handler;

    @BeforeEach
    void setUp() {
        handler = new SunsetUpdatedEventHandler(store);
    }

    @Test
    void persistsEventToStore() {
        var event = new SunsetUpdatedEvent(
                345,
                aSunset().withDate(LocalDate.parse("2021-06-01")).withAdditionalInformation("sunset info").build(),
                aPerson().build(),
                Instant.now());

        handler.handle(event);

        verify(store).save(event);
    }
}
