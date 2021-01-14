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
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdatePortfolioCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.PortfolioUpdatedEventHandler;
import uk.gov.digital.ho.systemregister.application.messaging.events.PortfolioUpdatedEvent;
import uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Instant;
import java.util.Map;
import javax.validation.Valid;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;

class UpdatePortfolioCommandHandlerTest {
    private final CurrentSystemRegisterState systemRegisterState = mock(CurrentSystemRegisterState.class);
    private final PortfolioUpdatedEventHandler eventHandler = mock(PortfolioUpdatedEventHandler.class);

    private UpdatePortfolioCommandHandler commandHandler;

    @BeforeEach
    void setUp() {
        commandHandler = new UpdatePortfolioCommandHandler(
                systemRegisterState,
                eventHandler,
                new CurrentStateCalculator());
    }

    @Test
    public void updatesPortfolioValueIfSystemExistsWithDifferentValue() throws Exception {
        SR_SystemBuilder partialSystem = aSystem().withId(345);
        givenCurrentStateWithSystem(partialSystem.withPortfolio("existing portfolio"));
        var eventTimestamp = Instant.now();
        var expectedAuthor = aPerson().withUsername("user").build();
        var command = new UpdatePortfolioCommand(
                345,
                "new portfolio",
                expectedAuthor,
                eventTimestamp);

        var updatedSystem = commandHandler.handle(command);

        var eventCaptor = ArgumentCaptor.forClass(PortfolioUpdatedEvent.class);
        verify(eventHandler).handle(eventCaptor.capture());
        assertThat(updatedSystem).usingRecursiveComparison()
                .isEqualTo(Tuple2.of(
                        partialSystem
                                .withPortfolio("new portfolio")
                                .build(),
                        new UpdateMetadata(expectedAuthor, eventTimestamp)));
        assertThat(eventCaptor.getValue()).usingRecursiveComparison()
                .isEqualTo(new PortfolioUpdatedEvent(
                        345,
                        "new portfolio",
                        expectedAuthor,
                        eventTimestamp));
    }

    @Test
    void raisesExceptionIfTheSystemCannotBeFound() {
        givenCurrentStateWithSystem(aSystem().withId(456));
        var command = new UpdatePortfolioCommand(678, "portfolio", aPerson().build(), Instant.now());

        assertThatThrownBy(() -> commandHandler.handle(command))
                .isInstanceOf(NoSuchSystemException.class)
                .hasMessageContaining("678");
    }

    @Test
    void raisesExceptionIfPortfolioIsUnchanged() {
        givenCurrentStateWithSystem(aSystem()
                .withId(456)
                .withPortfolio("portfolio"));
        var command = new UpdatePortfolioCommand(456, "portfolio", aPerson().build(), Instant.now());

        assertThatThrownBy(() -> commandHandler.handle(command))
                .isInstanceOf(CommandHasNoEffectException.class);
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
        UpdateMetadata metadata = new UpdateMetadata(aPerson().withUsername("username1").build(), Instant.now());

        when(systemRegisterState.getCurrentState())
                .thenReturn(new CurrentState(
                        Map.of(systemBuilder.build(), metadata),
                        Instant.now()));
    }
}
