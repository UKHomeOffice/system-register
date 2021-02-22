package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateCriticalityCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.CriticalityUpdatedEventHandler;

import javax.inject.Singleton;

@Singleton
public class UpdateCriticalityCommandHandler extends AbstractUpdateCommandHandler<UpdateCriticalityCommand> {
    public UpdateCriticalityCommandHandler(CurrentSystemRegisterState systemRegisterState, CriticalityUpdatedEventHandler handler, CurrentStateCalculator calculator) {
        super(systemRegisterState, handler, calculator);
    }
}
