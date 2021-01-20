package uk.gov.digital.ho.systemregister.application.messaging.eventhandlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.TechnicalOwnerUpdatedEventHandler;
import uk.gov.digital.ho.systemregister.application.messaging.events.TechnicalOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import java.time.Instant;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

class TechnicalOwnerUpdatedEventHandlerTest {
    private final IEventStore store = mock(IEventStore.class);

    private TechnicalOwnerUpdatedEventHandler handler;

    @BeforeEach
    void setUp() {
        handler = new TechnicalOwnerUpdatedEventHandler(store);
    }

    @Test
    void persistsEventToStore() {
        var event = new TechnicalOwnerUpdatedEvent(234, "tech owner", aPerson().build(), Instant.now());

        handler.handle(event);

        verify(store).save(event);
    }
}
