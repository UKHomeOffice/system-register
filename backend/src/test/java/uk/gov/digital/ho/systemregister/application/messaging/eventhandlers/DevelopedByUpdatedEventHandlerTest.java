package uk.gov.digital.ho.systemregister.application.messaging.eventhandlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.events.DevelopedByUpdatedEvent;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import java.time.Instant;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

class DevelopedByUpdatedEventHandlerTest {
    private final IEventStore store = mock(IEventStore.class);

    private DevelopedByUpdatedEventHandler handler;

    @BeforeEach
    void setUp() {
        handler = new DevelopedByUpdatedEventHandler(store);
    }

    @Test
    void persistsEventToStore() {
        var event = new DevelopedByUpdatedEvent(123, "owner", aPerson().build(), Instant.now());

        handler.handle(event);

        verify(store).save(event);
    }
}
