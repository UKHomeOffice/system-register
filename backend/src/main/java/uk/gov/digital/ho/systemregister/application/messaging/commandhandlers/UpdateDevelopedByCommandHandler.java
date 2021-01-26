package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateDevelopedByCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.DevelopedByUpdatedEventHandler;

import javax.inject.Singleton;

@Singleton
public class UpdateDevelopedByCommandHandler extends AbstractCommandHandler<UpdateDevelopedByCommand> {
    public UpdateDevelopedByCommandHandler(CurrentSystemRegisterState systemRegisterState, DevelopedByUpdatedEventHandler handler, CurrentStateCalculator calculator) {
        super(systemRegisterState, handler, calculator);
    }
}
