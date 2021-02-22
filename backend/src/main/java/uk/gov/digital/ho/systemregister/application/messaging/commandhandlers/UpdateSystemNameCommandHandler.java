package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateSystemNameCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.SystemNameUpdatedEventHandler;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.domain.SystemRegister;

import java.util.function.Predicate;
import javax.inject.Singleton;

@Singleton
public class UpdateSystemNameCommandHandler extends AbstractUpdateCommandHandler<UpdateSystemNameCommand> {
    public UpdateSystemNameCommandHandler(
            CurrentSystemRegisterState systemRegisterState,
            SystemNameUpdatedEventHandler eventHandler,
            CurrentStateCalculator calculator) {
        super(systemRegisterState, eventHandler, calculator);
    }

    @Override
    protected void checkCommand(UpdateSystemNameCommand command, SR_System system, SystemRegister register) throws CommandProcessingException {
        super.checkCommand(command, system, register);
        checkForDuplicateSystemName(command, register);
    }

    private static void checkForDuplicateSystemName(UpdateSystemNameCommand command, SystemRegister systemRegister) throws SystemNameNotUniqueException {
        if (systemRegister.getAllSystems().stream()
                .filter((s) -> s.id != command.getId())
                .map(s -> s.name.toLowerCase())
                .anyMatch(Predicate.isEqual(command.name.toLowerCase()))) {
            throw new SystemNameNotUniqueException(command.name);
        }
    }
}
