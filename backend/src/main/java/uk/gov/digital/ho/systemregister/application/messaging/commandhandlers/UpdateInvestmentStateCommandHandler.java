package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateInvestmentStateCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.InvestmentStateUpdatedEventHandler;

import javax.inject.Singleton;

@Singleton
public class UpdateInvestmentStateCommandHandler extends AbstractCommandHandler<UpdateInvestmentStateCommand> {
    public UpdateInvestmentStateCommandHandler(CurrentSystemRegisterState systemRegisterState, InvestmentStateUpdatedEventHandler handler, CurrentStateCalculator calculator) {
        super(systemRegisterState, handler, calculator);
    }
}
