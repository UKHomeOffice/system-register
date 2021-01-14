package uk.gov.digital.ho.systemregister.application.messaging.eventhandlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.events.InvestmentStateUpdatedEvent;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import java.time.Instant;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

class InvestmentStateUpdatedEventHandlerTest {
    private final IEventStore store = mock(IEventStore.class);

    private InvestmentStateUpdatedEventHandler handler;

    @BeforeEach
    void setUp() {
        handler = new InvestmentStateUpdatedEventHandler(store);
    }

    @Test
    void persistsEventToStore() {
        var event = new InvestmentStateUpdatedEvent(123, "portfolio", aPerson().build(), Instant.now());

        handler.handle(event);

        verify(store).save(event);
    }
}
