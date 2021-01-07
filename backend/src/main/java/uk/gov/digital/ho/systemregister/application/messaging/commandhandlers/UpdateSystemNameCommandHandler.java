package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import io.smallrye.mutiny.tuples.Tuple2;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.UpdateMetadata;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateProductOwnerCommand;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateSystemNameCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.SystemNameUpdatedEventHandler;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAddedEvent;
import uk.gov.digital.ho.systemregister.domain.AddSystemResult;
import uk.gov.digital.ho.systemregister.domain.Change;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.domain.SystemRegister;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;
import java.util.List;
import java.util.function.Predicate;

@ApplicationScoped
public class UpdateSystemNameCommandHandler {
    private final CurrentSystemRegisterState systemRegisterState;
    private final SystemNameUpdatedEventHandler eventHandler;
    private final CurrentStateCalculator calculator;

    public UpdateSystemNameCommandHandler(
            CurrentSystemRegisterState systemRegisterState,
            SystemNameUpdatedEventHandler eventHandler,
            CurrentStateCalculator calculator) {
        this.systemRegisterState = systemRegisterState;
        this.eventHandler = eventHandler;
        this.calculator = calculator;
    }

    private static void checkSystemNameWillBeChanged(UpdateSystemNameCommand command, SR_System system)
            throws CommandHasNoEffectException {
        if (!command.willUpdate(system)) {
            throw new CommandHasNoEffectException("system name is the same: " + system.name);
        }
    }

    public Tuple2<SR_System, UpdateMetadata> handle(@Valid UpdateSystemNameCommand command)
            throws NoSuchSystemException, CommandHasNoEffectException, SystemNameNotUniqueException {
        SystemRegister systemRegister = new SystemRegister(systemRegisterState.getCurrentState().getSystems());

        SR_System system = systemRegister.getSystemById(command.id)
                .orElseThrow(() -> new NoSuchSystemException(command.id));
        checkSystemNameWillBeChanged(command, system);

        checkForDupicateSystemName(command, systemRegister);

        var event = command.toEvent();
        eventHandler.handle(event);

        return Tuple2.of(
                calculator.applyUpdateToSystem(system, event),
                new UpdateMetadata(command.author, command.timestamp));
    }

    private void checkForDupicateSystemName(UpdateSystemNameCommand command, SystemRegister systemRegister) throws SystemNameNotUniqueException {
        if (systemRegister.getAllSystems().stream()
                .filter((s) -> s.id != command.id)
                .map(s -> s.name)
                .anyMatch(Predicate.isEqual(command.name))) {
            throw new SystemNameNotUniqueException("A system called " + command.name + " already exists");
        }
    }
}