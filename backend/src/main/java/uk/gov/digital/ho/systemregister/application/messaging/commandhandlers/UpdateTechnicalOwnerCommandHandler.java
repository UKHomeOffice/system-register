package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateTechnicalOwnerCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.TechnicalOwnerUpdatedEventHandler;

import javax.inject.Singleton;

@Singleton
public class UpdateTechnicalOwnerCommandHandler extends AbstractCommandHandler<UpdateTechnicalOwnerCommand> {
    public UpdateTechnicalOwnerCommandHandler(
            CurrentSystemRegisterState systemRegisterState,
            TechnicalOwnerUpdatedEventHandler eventHandler,
            CurrentStateCalculator currentStateCalculator) {
        super(systemRegisterState, eventHandler, currentStateCalculator);
    }
}
