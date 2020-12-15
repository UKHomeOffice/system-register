package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import io.smallrye.mutiny.tuples.Tuple2;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.UpdateMetadata;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateCriticalityCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.CriticalityUpdatedEventHandler;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.domain.SystemRegister;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;

@ApplicationScoped
public class UpdateCriticalityCommandHandler{
    private final CurrentSystemRegisterState systemRegisterState;
    private final CriticalityUpdatedEventHandler eventHandler;
    private final CurrentStateCalculator calculator;


    public UpdateCriticalityCommandHandler(
            CurrentSystemRegisterState systemRegisterState,
            CriticalityUpdatedEventHandler eventHandler,
            CurrentStateCalculator calculator) {
        this.systemRegisterState = systemRegisterState;
        this.eventHandler = eventHandler;
        this.calculator = calculator;
    }

    private static void checkCriticalityWillBeChanged(UpdateCriticalityCommand command, SR_System system)
            throws CommandHasNoEffectException {
        if (!command.willUpdate(system)) {
            throw new CommandHasNoEffectException("criticality level is the same: " + system.criticality);
        }
    }

    public Tuple2<SR_System, UpdateMetadata> handle(@Valid UpdateCriticalityCommand command)
            throws NoSuchSystemException, CommandHasNoEffectException {
        SystemRegister systemRegister = new SystemRegister(systemRegisterState.getCurrentState().getSystems());

        SR_System system = systemRegister.getSystemById(command.id)
                .orElseThrow(() -> new NoSuchSystemException(command.id));
        checkCriticalityWillBeChanged(command, system);

        var event = command.toEvent();
        eventHandler.handle(event);

        return Tuple2.of(
                calculator.applyUpdateToSystem(system, event),
                new UpdateMetadata(command.author, command.timestamp));
    }
}
