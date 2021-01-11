package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import io.smallrye.mutiny.tuples.Tuple2;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.UpdateMetadata;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateSystemDescriptionCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.SystemDescriptionUpdatedEventHandler;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.domain.SystemRegister;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;

@ApplicationScoped
public class UpdateSystemDescriptionCommandHandler {
    private final CurrentSystemRegisterState systemRegisterState;
    private final SystemDescriptionUpdatedEventHandler eventHandler;
    private final CurrentStateCalculator calculator;

    public UpdateSystemDescriptionCommandHandler(CurrentSystemRegisterState systemRegisterState, SystemDescriptionUpdatedEventHandler eventHandler, CurrentStateCalculator calculator) {
        this.systemRegisterState = systemRegisterState;
        this.eventHandler = eventHandler;
        this.calculator = calculator;
    }

    private static void checkSystemDescriptionWillBeChanged(UpdateSystemDescriptionCommand command, SR_System system)
            throws CommandHasNoEffectException {
        if (!command.willUpdate(system)) {
            throw new CommandHasNoEffectException("system description is the same: " + system.description);
        }
    }

    public Tuple2<SR_System, UpdateMetadata> handle(@Valid UpdateSystemDescriptionCommand command)
            throws CommandHasNoEffectException, NoSuchSystemException {
        var register = new SystemRegister(systemRegisterState.getCurrentState().getSystems());

        var system = register.getSystemById(command.id)
                .orElseThrow(() -> new NoSuchSystemException(command.id));
        checkSystemDescriptionWillBeChanged(command, system);

        var event = command.toEvent();
        eventHandler.handle(event);

        return Tuple2.of(
                calculator.applyUpdateToSystem(system, event),
                new UpdateMetadata(command.author, command.timestamp));
    }
}
