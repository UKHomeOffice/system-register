package uk.gov.digital.ho.systemregister.application.messaging.eventhandlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.events.ProductOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import java.time.Instant;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

class ProductOwnerUpdatedEventHandlerTest {
    private final IEventStore store = mock(IEventStore.class);

    private ProductOwnerUpdatedEventHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ProductOwnerUpdatedEventHandler(store);
    }

    @Test
    void persistsEventToStore() {
        var event = new ProductOwnerUpdatedEvent(123, "owner", aPerson().build(), Instant.now());

        handler.handle(event);

        verify(store).save(event);
    }
}
