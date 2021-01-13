package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import io.smallrye.mutiny.tuples.Tuple2;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.UpdateMetadata;
import uk.gov.digital.ho.systemregister.application.messaging.commands.Command;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.EventHandler;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.domain.SystemRegister;

import javax.validation.Valid;

public abstract class AbstractCommandHandler<T extends Command> {
    private final CurrentSystemRegisterState systemRegisterState;
    private final EventHandler eventHandler;
    private final CurrentStateCalculator calculator;

    public AbstractCommandHandler(CurrentSystemRegisterState systemRegisterState, EventHandler eventHandler, CurrentStateCalculator calculator) {
        this.systemRegisterState = systemRegisterState;
        this.eventHandler = eventHandler;
        this.calculator = calculator;
    }

    public Tuple2<SR_System, UpdateMetadata> handle(@Valid T command) throws NoSuchSystemException, CommandProcessingException {
        SystemRegister systemRegister = new SystemRegister(systemRegisterState.getCurrentState().getSystems());

        SR_System system = systemRegister.getSystemById(command.getId())
                .orElseThrow(() -> new NoSuchSystemException(command.getId()));
        checkCommand(command, system, systemRegister);

        var event = command.toEvent();
        eventHandler.handle(event);

        return Tuple2.of(
                calculator.applyUpdateToSystem(system, event),
                new UpdateMetadata(command.getAuthor(), command.getTimestamp()));
    }

    protected void checkCommand(T command, SR_System system, SystemRegister register) throws CommandProcessingException {
        command.ensureCommandUpdatesSystem(system);
    }
}
