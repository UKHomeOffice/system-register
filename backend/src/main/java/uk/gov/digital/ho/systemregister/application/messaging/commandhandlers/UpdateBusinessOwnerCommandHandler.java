package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateBusinessOwnerCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.BusinessOwnerUpdatedEventHandler;

import javax.inject.Singleton;

@Singleton
public class UpdateBusinessOwnerCommandHandler extends AbstractUpdateCommandHandler<UpdateBusinessOwnerCommand> {
    public UpdateBusinessOwnerCommandHandler(CurrentSystemRegisterState systemRegisterState, BusinessOwnerUpdatedEventHandler handler, CurrentStateCalculator calculator) {
        super(systemRegisterState, handler, calculator);
    }
}
