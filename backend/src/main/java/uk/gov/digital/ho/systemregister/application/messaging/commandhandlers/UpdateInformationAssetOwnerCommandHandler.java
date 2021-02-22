package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateInformationAssetOwnerCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.InformationAssetOwnerUpdatedEventHandler;

import javax.inject.Singleton;

@Singleton
public class UpdateInformationAssetOwnerCommandHandler extends AbstractUpdateCommandHandler<UpdateInformationAssetOwnerCommand> {
    public UpdateInformationAssetOwnerCommandHandler(CurrentSystemRegisterState systemRegisterState, InformationAssetOwnerUpdatedEventHandler handler, CurrentStateCalculator calculator) {
        super(systemRegisterState, handler, calculator);
    }
}
