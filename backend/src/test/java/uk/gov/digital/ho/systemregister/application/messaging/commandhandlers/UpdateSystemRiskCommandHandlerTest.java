package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.UpdateMetadata;
import uk.gov.digital.ho.systemregister.application.messaging.commands.Command;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateSystemRiskCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.SystemRiskUpdatedEventHandler;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemRiskUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Risk;
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
import static uk.gov.digital.ho.systemregister.domain.SR_RiskBuilder.aLowRisk;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

class UpdateSystemRiskCommandHandlerTest {
    private final CurrentSystemRegisterState systemRegisterState = mock(CurrentSystemRegisterState.class);
    private final SystemRiskUpdatedEventHandler eventHandler = mock(SystemRiskUpdatedEventHandler.class);

    private UpdateSystemRiskCommandHandler commandHandler;

    @BeforeEach
    void setUp() {
        commandHandler = new UpdateSystemRiskCommandHandler(
                systemRegisterState,
                eventHandler,
                new CurrentStateCalculator());
    }

    @Test
    public void updatesSystemRiskValueIfSystemExistsWithDifferentValue() throws Exception {
        SR_SystemBuilder partialSystem = aSystem().withId(456);
        givenCurrentStateWithSystem(partialSystem.withRisks(aLowRisk().withName("existing risk").withRationale("not very risky")));
        var eventTimestamp = Instant.now();
        var expectedAuthor = aPerson().withUsername("user").build();
        var command = new UpdateSystemRiskCommand(
                456,
                new UpdateSystemRiskCommand.Risk("existing risk", "high", "it became risky"),
                expectedAuthor,
                eventTimestamp);

        var updatedSystem = commandHandler.handle(command);

        var eventCaptor = ArgumentCaptor.forClass(SystemRiskUpdatedEvent.class);
        verify(eventHandler).handle(eventCaptor.capture());
        assertThat(updatedSystem.getItem1()).usingRecursiveComparison()
                .ignoringFields("risks")
                .isEqualTo(partialSystem.build());
        assertThat(updatedSystem.getItem1().risks)
                .containsExactly(new SR_Risk("existing risk", "high", "it became risky"));
        assertThat(updatedSystem.getItem2()).usingRecursiveComparison()
                .isEqualTo(new UpdateMetadata(expectedAuthor, eventTimestamp));
        assertThat(eventCaptor.getValue()).usingRecursiveComparison()
                .isEqualTo(new SystemRiskUpdatedEvent(
                        456,
                        new SR_Risk("existing risk", "high", "it became risky"),
                        expectedAuthor,
                        eventTimestamp));
    }

    @Test
    void raisesExceptionIfTheSystemCannotBeFound() {
        givenCurrentStateWithSystem(aSystem().withId(456));
        var command = new UpdateSystemRiskCommand(678, new UpdateSystemRiskCommand.Risk("", "", ""), aPerson().build(), Instant.now());

        assertThatThrownBy(() -> commandHandler.handle(command))
                .isInstanceOf(NoSuchSystemException.class)
                .hasMessageContaining("678");
    }

    @Test
    void raisesExceptionIfSystemRisksValueIsUnchanged() {
        givenCurrentStateWithSystem(aSystem()
                .withId(987)
                .withRisks(aLowRisk().withName("existing risk").withRationale("existing reason") ));
        var command = new UpdateSystemRiskCommand(987, new UpdateSystemRiskCommand.Risk("existing risk", "low","existing reason"), aPerson().build(), Instant.now());

        assertThatThrownBy(() -> commandHandler.handle(command))
                .isInstanceOf(CommandHasNoEffectException.class)
                .hasMessageContaining("system risks are the same");
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

    @Test
    void raisesExceptionIfRiskToBeUpdatedDoesNotExist() {
        givenCurrentStateWithSystem(aSystem()
                .withId(987)
                .withRisks(aLowRisk().withName("existing risk").withRationale("existing reason") ));
        var command = new UpdateSystemRiskCommand(987, new UpdateSystemRiskCommand
                .Risk("a different risk", "high","some reason"), aPerson().build(), Instant.now());

        assertThatThrownBy(() -> commandHandler.handle(command))
                .isInstanceOf(RiskDoesNotExistException.class)
                .hasMessageContaining("system does not have risk: a different risk");
    }
}
