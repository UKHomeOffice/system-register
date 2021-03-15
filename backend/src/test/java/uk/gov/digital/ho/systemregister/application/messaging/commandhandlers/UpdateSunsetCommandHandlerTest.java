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
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateSunsetCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.SunsetUpdatedEventHandler;
import uk.gov.digital.ho.systemregister.application.messaging.events.SunsetUpdatedEvent;
import uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder;

import javax.validation.Valid;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.domain.SR_SunsetBuilder.aSunset;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

class UpdateSunsetCommandHandlerTest {
    private final CurrentSystemRegisterState systemRegisterState = mock(CurrentSystemRegisterState.class);
    private final SunsetUpdatedEventHandler eventHandler = mock(SunsetUpdatedEventHandler.class);

    private UpdateSunsetCommandHandler commandHandler;

    @BeforeEach
    void setUp() {
        commandHandler = new UpdateSunsetCommandHandler(
                systemRegisterState,
                eventHandler,
                new CurrentStateCalculator());
    }

    @Test
    public void updatesSunsetValueIfSystemExistsWithDifferentValue() throws Exception {
        SR_SystemBuilder partialSystem = aSystem().withId(678);
        givenCurrentStateWithSystem(partialSystem.withSunset(aSunset().withDate(LocalDate.parse("2020-06-01")).withAdditionalInformation("sunset info")));
        var eventTimestamp = Instant.now();
        var expectedAuthor = aPerson().withUsername("user").build();
        var command = new UpdateSunsetCommand(
                678,
                new UpdateSunsetCommand.Sunset(LocalDate.parse("2021-09-01"), "updated sunset info"),
                expectedAuthor,
                eventTimestamp);

        var updatedSystem = commandHandler.handle(command);

        var eventCaptor = ArgumentCaptor.forClass(SunsetUpdatedEvent.class);
        verify(eventHandler).handle(eventCaptor.capture());
        assertThat(updatedSystem).usingRecursiveComparison()
                .isEqualTo(Tuple2.of(
                        partialSystem
                                .withSunset(aSunset().withDate(LocalDate.parse("2021-09-01")).withAdditionalInformation("updated sunset info"))
                                .build(),
                        new UpdateMetadata(expectedAuthor, eventTimestamp)));
        assertThat(eventCaptor.getValue()).usingRecursiveComparison()
                .isEqualTo(new SunsetUpdatedEvent(
                        678,
                        aSunset().withDate(LocalDate.parse("2021-09-01")).withAdditionalInformation("updated sunset info").build(),
                        expectedAuthor,
                        eventTimestamp));
    }

    @Test
    void raisesExceptionIfTheSystemCannotBeFound() {
        givenCurrentStateWithSystem(aSystem().withId(456));
        var command = new UpdateSunsetCommand(
                789,
                new UpdateSunsetCommand.Sunset(LocalDate.parse("2021-09-01"), "updated sunset info"),
                aPerson().build(),
                Instant.now());

        assertThatThrownBy(() -> commandHandler.handle(command))
                .isInstanceOf(NoSuchSystemException.class)
                .hasMessageContaining("789");
    }

    @Test
    void raisesExceptionIfSunsetValueIsUnchanged() {
        givenCurrentStateWithSystem(aSystem()
                .withId(876)
                .withSunset(aSunset().withDate(LocalDate.parse("2021-06-01")).withAdditionalInformation("sunset info")));
        var command = new UpdateSunsetCommand(
                876,
                new UpdateSunsetCommand.Sunset(LocalDate.parse("2021-06-01"), "sunset info"),
                aPerson().build(), Instant.now());

        assertThatThrownBy(() -> commandHandler.handle(command))
                .isInstanceOf(CommandHasNoEffectException.class)
                .hasMessageContaining("sunset is the same: date=2021-06-01, additional information=sunset info");
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
