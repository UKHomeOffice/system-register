package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateSystemRiskCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.SystemRiskUpdatedEventHandler;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.domain.SystemRegister;

import javax.inject.Singleton;

@Singleton
public class UpdateSystemRiskCommandHandler extends AbstractUpdateCommandHandler<UpdateSystemRiskCommand> {
    public UpdateSystemRiskCommandHandler(
            CurrentSystemRegisterState systemRegisterState,
            SystemRiskUpdatedEventHandler eventHandler,
            CurrentStateCalculator calculator) {
        super(systemRegisterState, eventHandler, calculator);
    }

    @Override
    protected void checkCommand(UpdateSystemRiskCommand command, SR_System system, SystemRegister register) throws CommandProcessingException {
        command.ensureRiskExistsOnSystem(system);
        super.checkCommand(command, system, register);
    }
}
