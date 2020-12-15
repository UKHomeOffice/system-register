package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import io.smallrye.mutiny.tuples.Tuple2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.UpdateMetadata;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateProductOwnerCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.ProductOwnerUpdatedEventHandler;
import uk.gov.digital.ho.systemregister.application.messaging.events.ProductOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

class UpdateProductOwnerCommandHandlerTest {
    private final CurrentSystemRegisterState systemRegisterState = mock(CurrentSystemRegisterState.class);
    private final ProductOwnerUpdatedEventHandler eventHandler = mock(ProductOwnerUpdatedEventHandler.class);

    private UpdateProductOwnerCommandHandler commandHandler;

    @BeforeEach
    void setUp() {
        commandHandler = new UpdateProductOwnerCommandHandler(
                systemRegisterState,
                eventHandler,
                new CurrentStateCalculator());
    }

    @Test
    public void updatesProductOwnerValueIfSystemExistsWithDifferentValue() throws Exception {
        SR_SystemBuilder partialSystem = aSystem().withId(123);
        givenCurrentStateWithSystem(partialSystem.withProductOwner("Reginald Humphries"));
        var eventTimestamp = Instant.now();
        var expectedAuthor = aPerson().withUsername("username2").build();
        var command = new UpdateProductOwnerCommand(
                123,
                "Betty Franklin",
                expectedAuthor,
                eventTimestamp);

        var updatedSystem = commandHandler.handle(command);

        var eventCaptor = ArgumentCaptor.forClass(ProductOwnerUpdatedEvent.class);
        verify(eventHandler).handle(eventCaptor.capture());
        assertThat(updatedSystem).usingRecursiveComparison()
                .isEqualTo(Tuple2.of(
                        partialSystem
                                .withProductOwner("Betty Franklin")
                                .build(),
                        new UpdateMetadata(expectedAuthor, eventTimestamp)));
        assertThat(eventCaptor.getValue()).usingRecursiveComparison()
                .isEqualTo(new ProductOwnerUpdatedEvent(
                        123,
                        "Betty Franklin",
                        expectedAuthor,
                        eventTimestamp));
    }

    @Test
    void raisesExceptionIfTheSystemCannotBeFound() {
        givenCurrentStateWithSystem(aSystem().withId(456));
        var command = new UpdateProductOwnerCommand(789, "owner", aPerson().build(), Instant.now());

        assertThatThrownBy(() -> commandHandler.handle(command))
                .isInstanceOf(NoSuchSystemException.class)
                .hasMessageContaining("789");
    }

    @Test
    void raisesExceptionIfProductOwnerValueIsUnchanged() {
        givenCurrentStateWithSystem(aSystem()
                .withId(345)
                .withProductOwner("original owner"));
        var command = new UpdateProductOwnerCommand(345, "original owner", aPerson().build(), Instant.now());

        assertThatThrownBy(() -> commandHandler.handle(command))
                .isInstanceOf(CommandHasNoEffectException.class)
                .hasMessageContaining("product owner is the same: original owner");
    }

    private void givenCurrentStateWithSystem(SR_SystemBuilder systemBuilder) {
        UpdateMetadata metadata = new UpdateMetadata(aPerson().withUsername("username1").build(), Instant.now());

        when(systemRegisterState.getCurrentState())
                .thenReturn(new CurrentState(
                        Map.of(systemBuilder.build(), metadata),
                        Instant.now()));
    }
}
