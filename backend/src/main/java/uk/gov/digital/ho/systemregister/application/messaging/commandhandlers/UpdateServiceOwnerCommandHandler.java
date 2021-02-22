package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateServiceOwnerCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.ServiceOwnerUpdatedEventHandler;

import javax.inject.Singleton;

@Singleton
public class UpdateServiceOwnerCommandHandler extends AbstractUpdateCommandHandler<UpdateServiceOwnerCommand> {
    public UpdateServiceOwnerCommandHandler(
            CurrentSystemRegisterState systemRegisterState,
            ServiceOwnerUpdatedEventHandler eventHandler,
            CurrentStateCalculator currentStateCalculator) {
        super(systemRegisterState, eventHandler, currentStateCalculator);
    }
}
