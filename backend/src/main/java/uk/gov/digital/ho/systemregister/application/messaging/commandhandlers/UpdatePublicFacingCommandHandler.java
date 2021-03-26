package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateInvestmentStateCommand;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdatePublicFacingCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.InvestmentStateUpdatedEventHandler;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.PublicFacingUpdatedEventHandler;

import javax.inject.Singleton;

@Singleton
public class UpdatePublicFacingCommandHandler extends AbstractUpdateCommandHandler<UpdatePublicFacingCommand> {
    public UpdatePublicFacingCommandHandler(CurrentSystemRegisterState systemRegisterState, PublicFacingUpdatedEventHandler handler, CurrentStateCalculator calculator) {
        super(systemRegisterState, handler, calculator);
    }
}
