package uk.gov.digital.ho.systemregister.application.messaging.eventhandlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.events.ServiceOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.application.messaging.events.TechnicalOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import java.time.Instant;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

class ServiceOwnerUpdatedEventHandlerTest {
    private final IEventStore store = mock(IEventStore.class);

    private ServiceOwnerUpdatedEventHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ServiceOwnerUpdatedEventHandler(store);
    }

    @Test
    void persistsEventToStore() {
        var event = new ServiceOwnerUpdatedEvent(234, "service owner", aPerson().build(), Instant.now());

        handler.handle(event);

        verify(store).save(event);
    }
}
