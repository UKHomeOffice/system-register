package uk.gov.digital.ho.systemregister.application.messaging.eventhandlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.events.CriticalityUpdatedEvent;
import uk.gov.digital.ho.systemregister.application.messaging.events.PortfolioUpdatedEvent;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import java.time.Instant;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

class CriticalityUpdatedEventHandlerTest {
    private final IEventStore store = mock(IEventStore.class);

    private CriticalityUpdatedEventHandler handler;

    @BeforeEach
    void setUp() {
        handler = new CriticalityUpdatedEventHandler(store);
    }

    @Test
    void persistsEventToStore() {
        var event = new CriticalityUpdatedEvent(aPerson().build(), Instant.now(),123, "portfolio");

        handler.handle(event);

        verify(store).save(event);
    }
}
