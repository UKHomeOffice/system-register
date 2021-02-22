package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import io.smallrye.mutiny.tuples.Tuple2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.UpdateMetadata;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateCommand;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateCriticalityCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.CriticalityUpdatedEventHandler;
import uk.gov.digital.ho.systemregister.application.messaging.events.CriticalityUpdatedEvent;
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

class UpdateCriticalityCommandHandlerTest {
    private final CurrentSystemRegisterState systemRegisterState = mock(CurrentSystemRegisterState.class);
    private final CriticalityUpdatedEventHandler eventHandler = mock(CriticalityUpdatedEventHandler.class);

    private UpdateCriticalityCommandHandler commandHandler;

    @BeforeEach
    void setUp() {
        commandHandler = new UpdateCriticalityCommandHandler(
                systemRegisterState,
                 eventHandler,
                new CurrentStateCalculator());
    }

    @Test
    public void updatesCriticalityValueIfSystemExistsWithDifferentValue() throws Exception {
        SR_SystemBuilder partialSystem = aSystem().withId(123);
        givenCurrentStateWithSystem(partialSystem.withCriticality("unknown"));
        var eventTimestamp = Instant.now();
        var expectedAuthor = aPerson().withUsername("username2").build();
        var command = new UpdateCriticalityCommand(123, "high", expectedAuthor, eventTimestamp);

        var updatedSystem = commandHandler.handle(command);

        var eventCaptor = ArgumentCaptor.forClass(CriticalityUpdatedEvent.class);
        verify(eventHandler).handle(eventCaptor.capture());
        assertThat(updatedSystem).usingRecursiveComparison()
                .isEqualTo(Tuple2.of(
                        partialSystem
                                .withCriticality("high")
                                .build(),
                        new UpdateMetadata(expectedAuthor, eventTimestamp)));
        assertThat(eventCaptor.getValue()).usingRecursiveComparison()
                .isEqualTo(new CriticalityUpdatedEvent(
                        expectedAuthor,
                        eventTimestamp,
                        123,
                        "high"));
    }

    @Test
    void raisesExceptionIfTheSystemCannotBeFound() {
        givenCurrentStateWithSystem(aSystem().withId(456));
        var command = new UpdateCriticalityCommand(789, "low", aPerson().build(), Instant.now());

        assertThatThrownBy(() -> commandHandler.handle(command))
                .isInstanceOf(NoSuchSystemException.class)
                .hasMessageContaining("789");
    }

    @Test
    void raisesExceptionIfCriticalityValueIsUnchanged() {
        givenCurrentStateWithSystem(aSystem()
                .withId(345)
                .withCriticality("low"));
        var command = new UpdateCriticalityCommand(345, "low", aPerson().build(), Instant.now());

        assertThatThrownBy(() -> commandHandler.handle(command))
                .isInstanceOf(CommandHasNoEffectException.class)
                .hasMessageContaining("criticality level is the same: low");
    }

    @Test
    void validatesCommand() throws NoSuchMethodException {
        Method handleMethod = commandHandler.getClass()
                .getMethod("handle", UpdateCommand.class);
        Parameter commandArgument = handleMethod.getParameters()[0];

        boolean hasValidAnnotation = commandArgument.isAnnotationPresent(Valid.class);

        assertThat(hasValidAnnotation).isTrue();
    }

    private void givenCurrentStateWithSystem(SR_SystemBuilder systemBuilder) {
        UpdateMetadata metadata = new UpdateMetadata(aPerson().withUsername("username1").build(), Instant.now());

        when(systemRegisterState.getCurrentState())
                .thenReturn(new CurrentState(
                        Map.of(systemBuilder.build(), metadata),
                        Instant.now()));
    }
}
