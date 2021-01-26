package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateSupportedByCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.SupportedByUpdatedEventHandler;

import javax.inject.Singleton;

@Singleton
public class UpdateSupportedByCommandHandler extends AbstractCommandHandler<UpdateSupportedByCommand> {
    public UpdateSupportedByCommandHandler(
            CurrentSystemRegisterState systemRegisterState,
            SupportedByUpdatedEventHandler eventHandler,
            CurrentStateCalculator currentStateCalculator) {
        super(systemRegisterState, eventHandler, currentStateCalculator);
    }
}
