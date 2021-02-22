package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateProductOwnerCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.ProductOwnerUpdatedEventHandler;

import javax.inject.Singleton;

@Singleton
public class UpdateProductOwnerCommandHandler extends AbstractUpdateCommandHandler<UpdateProductOwnerCommand> {
    public UpdateProductOwnerCommandHandler(CurrentSystemRegisterState systemRegisterState, ProductOwnerUpdatedEventHandler handler, CurrentStateCalculator calculator) {
        super(systemRegisterState, handler, calculator);
    }
}
