package uk.gov.digital.ho.systemregister.application.messaging.eventhandlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAliasesUpdatedEvent;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import java.time.Instant;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

class SystemAliasesUpdatedEventHandlerTest {
    private final IEventStore store = mock(IEventStore.class);

    private SystemAliasesUpdatedEventHandler handler;

    @BeforeEach
    void setUp() {
        handler = new SystemAliasesUpdatedEventHandler(store);
    }

    @Test
    void persistsEventToStore() {
        var event = new SystemAliasesUpdatedEvent(234, Set.of("alias"), aPerson().build(), Instant.now());

        handler.handle(event);

        verify(store).save(event);
    }
}
