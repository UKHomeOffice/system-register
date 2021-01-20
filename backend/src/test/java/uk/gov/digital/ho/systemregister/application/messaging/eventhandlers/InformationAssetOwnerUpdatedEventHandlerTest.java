package uk.gov.digital.ho.systemregister.application.messaging.eventhandlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.events.InformationAssetOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import java.time.Instant;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

class InformationAssetOwnerUpdatedEventHandlerTest {
    private final IEventStore store = mock(IEventStore.class);

    private InformationAssetOwnerUpdatedEventHandler handler;

    @BeforeEach
    void setUp() {
        handler = new InformationAssetOwnerUpdatedEventHandler(store);
    }

    @Test
    void persistsEventToStore() {
        var event = new InformationAssetOwnerUpdatedEvent(123, "owner", aPerson().build(), Instant.now());

        handler.handle(event);

        verify(store).save(event);
    }
}
