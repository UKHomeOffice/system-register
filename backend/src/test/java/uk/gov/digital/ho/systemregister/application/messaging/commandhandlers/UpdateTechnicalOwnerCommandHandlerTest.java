package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import io.smallrye.mutiny.tuples.Tuple2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.UpdateMetadata;
import uk.gov.digital.ho.systemregister.application.messaging.commands.Command;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateTechnicalOwnerCommand;
import uk.gov.digital.ho.systemregister.application.messaging.events.TechnicalOwnerUpdatedEvent;
import uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder;

import javax.validation.Valid;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

class UpdateTechnicalOwnerCommandHandlerTest {
    private final CurrentSystemRegisterState systemRegisterState = mock(CurrentSystemRegisterState.class);
    private final TechnicalOwnerUpdatedEventHandler eventHandler = mock(TechnicalOwnerUpdatedEventHandler.class);

    private UpdateTechnicalOwnerCommandHandler commandHandler;

    @BeforeEach
    void setUp() {
        commandHandler = new UpdateTechnicalOwnerCommandHandler(
                systemRegisterState,
                eventHandler,
                new CurrentStateCalculator());
    }

    @Test
    public void updatesTechnicalOwnerValueIfSystemExistsWithDifferentValue() throws Exception {
        SR_SystemBuilder partialSystem = aSystem().withId(456);
        givenCurrentStateWithSystem(partialSystem.withTechnicalOwner("Tech Owner"));
        var eventTimestamp = Instant.now();
        var expectedAuthor = aPerson().withUsername("user").build();
        var command = new UpdateTechnicalOwnerCommand(
                456,
                "New Tech Owner",
                expectedAuthor,
                eventTimestamp);

        var updatedSystem = commandHandler.handle(command);

        var eventCaptor = ArgumentCaptor.forClass(TechnicalOwnerUpdatedEvent.class);
        verify(eventHandler).handle(eventCaptor.capture());
        assertThat(updatedSystem).usingRecursiveComparison()
                .isEqualTo(Tuple2.of(
                        partialSystem
                                .withTechnicalOwner("New Tech Owner")
                                .build(),
                        new UpdateMetadata(expectedAuthor, eventTimestamp)));
        assertThat(eventCaptor.getValue()).usingRecursiveComparison()
                .isEqualTo(new TechnicalOwnerUpdatedEvent(
                        456,
                        "New Tech Owner",
                        expectedAuthor,
                        eventTimestamp));
    }

    @Test
    void raisesExceptionIfTheSystemCannotBeFound() {
        givenCurrentStateWithSystem(aSystem().withId(456));
        var command = new UpdateTechnicalOwnerCommand(678, "owner", aPerson().build(), Instant.now());

        assertThatThrownBy(() -> commandHandler.handle(command))
                .isInstanceOf(NoSuchSystemException.class)
                .hasMessageContaining("678");
    }

    @Test
    void raisesExceptionIfTechnicalOwnerValueIsUnchanged() {
        givenCurrentStateWithSystem(aSystem()
                .withId(987)
                .withTechnicalOwner("owner"));
        var command = new UpdateTechnicalOwnerCommand(987, "owner", aPerson().build(), Instant.now());

        assertThatThrownBy(() -> commandHandler.handle(command))
                .isInstanceOf(CommandHasNoEffectException.class)
                .hasMessageContaining("technical owner is the same: owner");
    }

    @Test
    void validatesCommand() throws NoSuchMethodException {
        Method handleMethod = commandHandler.getClass()
                .getMethod("handle", Command.class);
        Parameter commandArgument = handleMethod.getParameters()[0];

        boolean hasValidAnnotation = commandArgument.isAnnotationPresent(Valid.class);

        assertThat(hasValidAnnotation).isTrue();
    }

    private void givenCurrentStateWithSystem(SR_SystemBuilder systemBuilder) {
        UpdateMetadata metadata = new UpdateMetadata(aPerson().withUsername("a user").build(), Instant.now());

        when(systemRegisterState.getCurrentState())
                .thenReturn(new CurrentState(
                        Map.of(systemBuilder.build(), metadata),
                        Instant.now()));
    }
}
