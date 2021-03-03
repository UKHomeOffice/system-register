package uk.gov.digital.ho.systemregister.application.messaging.eventhandlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemRiskUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Risk;
import uk.gov.digital.ho.systemregister.io.database.IEventStore;

import java.time.Instant;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

class SystemRiskUpdatedEventHandlerTest {
    private final IEventStore store = mock(IEventStore.class);

    private SystemRiskUpdatedEventHandler handler;

    @BeforeEach
    void setUp() {
        handler = new SystemRiskUpdatedEventHandler(store);
    }

    @Test
    void persistsEventToStore() {
        var event = new SystemRiskUpdatedEvent(234, new SR_Risk("some risk", "low", "a reason"), aPerson().build(), Instant.now());

        handler.handle(event);

        verify(store).save(event);
    }
}
