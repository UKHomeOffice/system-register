package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdatePortfolioCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.PortfolioUpdatedEventHandler;

import javax.inject.Singleton;

@Singleton
public class UpdatePortfolioCommandHandler extends AbstractUpdateCommandHandler<UpdatePortfolioCommand> {
    public UpdatePortfolioCommandHandler(CurrentSystemRegisterState systemRegisterState, PortfolioUpdatedEventHandler handler, CurrentStateCalculator calculator) {
        super(systemRegisterState, handler, calculator);
    }
}
