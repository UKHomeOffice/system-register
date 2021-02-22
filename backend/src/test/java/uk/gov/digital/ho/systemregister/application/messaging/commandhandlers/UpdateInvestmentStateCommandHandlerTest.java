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
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateInvestmentStateCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.InvestmentStateUpdatedEventHandler;
import uk.gov.digital.ho.systemregister.application.messaging.events.InvestmentStateUpdatedEvent;
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

class UpdateInvestmentStateCommandHandlerTest {
    private final CurrentSystemRegisterState systemRegisterState = mock(CurrentSystemRegisterState.class);
    private final InvestmentStateUpdatedEventHandler eventHandler = mock(InvestmentStateUpdatedEventHandler.class);

    private UpdateInvestmentStateCommandHandler commandHandler;

    @BeforeEach
    void setUp() {
        commandHandler = new UpdateInvestmentStateCommandHandler(
                systemRegisterState,
                eventHandler,
                new CurrentStateCalculator());
    }

    @Test
    public void updatesInvestmentStateValueIfSystemExistsWithDifferentValue() throws Exception {
        SR_SystemBuilder partialSystem = aSystem().withId(123);
        givenCurrentStateWithSystem(partialSystem.withInvestmentState("sunset"));
        var eventTimestamp = Instant.now();
        var expectedAuthor = aPerson().withUsername("username2").build();
        var command = new UpdateInvestmentStateCommand(123, "decommissioned", expectedAuthor, eventTimestamp);

        var updatedSystem = commandHandler.handle(command);

        var eventCaptor = ArgumentCaptor.forClass(InvestmentStateUpdatedEvent.class);
        verify(eventHandler).handle(eventCaptor.capture());
        assertThat(updatedSystem).usingRecursiveComparison()
                .isEqualTo(Tuple2.of(
                        partialSystem
                                .withInvestmentState("decommissioned")
                                .build(),
                        new UpdateMetadata(expectedAuthor, eventTimestamp)));
        assertThat(eventCaptor.getValue()).usingRecursiveComparison()
                .isEqualTo(new InvestmentStateUpdatedEvent(
                        123,
                        "decommissioned",
                        expectedAuthor,
                        eventTimestamp));
    }

    @Test
    void raisesExceptionIfTheSystemCannotBeFound() {
        givenCurrentStateWithSystem(aSystem().withId(456));
        var command = new UpdateInvestmentStateCommand(789, "invest", aPerson().build(), Instant.now());

        assertThatThrownBy(() -> commandHandler.handle(command))
                .isInstanceOf(NoSuchSystemException.class)
                .hasMessageContaining("789");
    }

    @Test
    void raisesExceptionIfInvestmentStateValueIsUnchanged() {
        givenCurrentStateWithSystem(aSystem()
                .withId(345)
                .withInvestmentState("invest"));
        var command = new UpdateInvestmentStateCommand(345, "invest", aPerson().build(), Instant.now());

        assertThatThrownBy(() -> commandHandler.handle(command))
                .isInstanceOf(CommandHasNoEffectException.class)
                .hasMessageContaining("investment state is the same: invest");
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
