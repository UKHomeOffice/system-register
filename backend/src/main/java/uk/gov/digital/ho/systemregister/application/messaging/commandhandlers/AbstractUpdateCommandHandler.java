package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.EventHandler;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.domain.SystemRegister;

public abstract class AbstractUpdateCommandHandler<T extends UpdateCommand> extends AbstractCommandHandler<T> {
    protected final CurrentStateCalculator calculator;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public AbstractUpdateCommandHandler(CurrentSystemRegisterState systemRegisterState, EventHandler eventHandler, CurrentStateCalculator calculator) {
        super(systemRegisterState, eventHandler);
        this.calculator = calculator;
    }

    @Override
    protected SR_System findSystem(T command, SystemRegister systemRegister) throws NoSuchSystemException, CommandProcessingException {
        SR_System system = systemRegister.getSystemById(command.getId())
                .orElseThrow(() -> new NoSuchSystemException(command.getId()));
        checkCommand(command, system, systemRegister);
        return system;
    }

    @Override
    protected SR_System applyCommand(T command, SR_System system) {
        var event = command.toEvent();
        eventHandler.handle(event);

        return calculator.applyUpdateToSystem(system, event);
    }

    protected void checkCommand(T command, SR_System system, SystemRegister register) throws CommandProcessingException {
        command.ensureCommandUpdatesSystem(system);
    }
}
