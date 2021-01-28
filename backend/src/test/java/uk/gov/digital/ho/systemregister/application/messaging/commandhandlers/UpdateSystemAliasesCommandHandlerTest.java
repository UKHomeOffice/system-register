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
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateSystemAliasesCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.SystemAliasesUpdatedEventHandler;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAliasesUpdatedEvent;
import uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder;

import javax.validation.Valid;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

class UpdateSystemAliasesCommandHandlerTest {
    private final CurrentSystemRegisterState systemRegisterState = mock(CurrentSystemRegisterState.class);
    private final SystemAliasesUpdatedEventHandler eventHandler = mock(SystemAliasesUpdatedEventHandler.class);

    private UpdateSystemAliasesCommandHandler commandHandler;

    @BeforeEach
    void setUp() {
        commandHandler = new UpdateSystemAliasesCommandHandler(
                systemRegisterState,
                eventHandler,
                new CurrentStateCalculator());
    }

    @Test
    public void updatesSystemAliasesValueIfSystemExistsWithDifferentValue() throws Exception {
        SR_SystemBuilder partialSystem = aSystem().withId(456);
        givenCurrentStateWithSystem(partialSystem.withAliases("existing alias"));
        var eventTimestamp = Instant.now();
        var expectedAuthor = aPerson().withUsername("user").build();
        var command = new UpdateSystemAliasesCommand(
                456,
                List.of("existing alias", "another alias"),
                expectedAuthor,
                eventTimestamp);

        var updatedSystem = commandHandler.handle(command);

        var eventCaptor = ArgumentCaptor.forClass(SystemAliasesUpdatedEvent.class);
        verify(eventHandler).handle(eventCaptor.capture());
        assertThat(updatedSystem.getItem1()).usingRecursiveComparison()
                .ignoringFields("aliases")
                .isEqualTo(partialSystem.build());
        assertThat(updatedSystem.getItem1().aliases)
                .containsExactlyInAnyOrder("existing alias", "another alias");
        assertThat(updatedSystem.getItem2()).usingRecursiveComparison()
                .isEqualTo(new UpdateMetadata(expectedAuthor, eventTimestamp));
        assertThat(eventCaptor.getValue()).usingRecursiveComparison()
                .isEqualTo(new SystemAliasesUpdatedEvent(
                        456,
                        Set.of("existing alias", "another alias"),
                        expectedAuthor,
                        eventTimestamp));
    }

    @Test
    void raisesExceptionIfTheSystemCannotBeFound() {
        givenCurrentStateWithSystem(aSystem().withId(456));
        var command = new UpdateSystemAliasesCommand(678, List.of(), aPerson().build(), Instant.now());

        assertThatThrownBy(() -> commandHandler.handle(command))
                .isInstanceOf(NoSuchSystemException.class)
                .hasMessageContaining("678");
    }

    @Test
    void raisesExceptionIfSystemAliasesValueIsUnchanged() {
        givenCurrentStateWithSystem(aSystem()
                .withId(987)
                .withAliases("existing alias"));
        var command = new UpdateSystemAliasesCommand(987, List.of("existing alias"), aPerson().build(), Instant.now());

        assertThatThrownBy(() -> commandHandler.handle(command))
                .isInstanceOf(CommandHasNoEffectException.class)
                .hasMessageContaining("system aliases are the same: [existing alias]");
    }

    @Test
    void doesNotAllowAliasStringsToBeDuplicateValues() {
        givenCurrentStateWithSystem(aSystem()
                .withId(987)
                .withAliases("existing alias"));

        var command = new UpdateSystemAliasesCommand(987, List.of("ok alias", "duplicate alias", "duplicate alias"), aPerson().build(), Instant.now());

        assertThatThrownBy(() -> commandHandler.handle(command))
                .isInstanceOf(DuplicateAliasProvidedException.class)
                .hasMessageContaining("system aliases contains duplicate value: [duplicate alias]");
    }

    @Test
    void raisesExceptionWithAllDuplicateValues() {
        givenCurrentStateWithSystem(aSystem()
                .withId(987)
                .withAliases("existing alias"));

        var command = new UpdateSystemAliasesCommand(
                987,
                List.of("ok alias", "duplicate alias", "duplicate alias", "another dupe", "another dupe"),
                aPerson().build(),
                Instant.now());

        assertThatThrownBy(() -> commandHandler.handle(command))
                .isInstanceOf(DuplicateAliasProvidedException.class)
                .hasMessageContaining("system aliases contains duplicate value: [duplicate alias, another dupe]");
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
