package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import io.smallrye.mutiny.tuples.Tuple2;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.UpdateMetadata;
import uk.gov.digital.ho.systemregister.application.messaging.commands.Command;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.EventHandler;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.domain.SystemRegister;

import javax.validation.Valid;

public abstract class AbstractCommandHandler<T extends Command> {
    protected final CurrentSystemRegisterState systemRegisterState;
    protected final EventHandler eventHandler;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public AbstractCommandHandler(CurrentSystemRegisterState systemRegisterState, EventHandler eventHandler) {
        this.systemRegisterState = systemRegisterState;
        this.eventHandler = eventHandler;
    }

    public Tuple2<SR_System, UpdateMetadata> handle(@Valid T command) throws NoSuchSystemException, CommandProcessingException {
        SystemRegister systemRegister = new SystemRegister(systemRegisterState.getCurrentState().getSystems());

        SR_System system = findSystem(command, systemRegister);
        var updatedSystem = applyCommand(command, system);

        return Tuple2.of(
                updatedSystem,
                new UpdateMetadata(command.getAuthor(), command.getTimestamp()));
    }

    abstract protected SR_System findSystem(T command, SystemRegister systemRegister) throws NoSuchSystemException, CommandProcessingException;

    abstract protected SR_System applyCommand(T command, SR_System system);
}
