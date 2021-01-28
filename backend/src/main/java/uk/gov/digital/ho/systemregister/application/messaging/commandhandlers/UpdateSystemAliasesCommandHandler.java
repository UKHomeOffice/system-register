package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateSystemAliasesCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.SystemAliasesUpdatedEventHandler;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.domain.SystemRegister;

import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class UpdateSystemAliasesCommandHandler extends AbstractCommandHandler<UpdateSystemAliasesCommand> {
    public UpdateSystemAliasesCommandHandler(
            CurrentSystemRegisterState systemRegisterState,
            SystemAliasesUpdatedEventHandler eventHandler,
            CurrentStateCalculator calculator) {
        super(systemRegisterState, eventHandler, calculator);
    }

    @Override
    protected void checkCommand(UpdateSystemAliasesCommand command, SR_System system, SystemRegister register) throws CommandProcessingException {
        super.checkCommand(command, system, register);
        checkForDuplicateSystemAliases(command);
    }

    private static void checkForDuplicateSystemAliases(UpdateSystemAliasesCommand command) throws DuplicateAliasProvidedException {
        Set<String> nonDuplicateAliases = new HashSet<>();
        Set<String> duplicateAliases = new HashSet<>();
        for (String alias : command.aliases) {
            boolean added = nonDuplicateAliases.add(alias);
            if (!added) {
                duplicateAliases.add(alias);
            }
        }

        if (!duplicateAliases.isEmpty()) {
            throw new DuplicateAliasProvidedException(duplicateAliases);
        }
    }
}
