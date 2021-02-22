package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateSystemDescriptionCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.SystemDescriptionUpdatedEventHandler;

import javax.inject.Singleton;

@Singleton
public class UpdateSystemDescriptionCommandHandler extends AbstractUpdateCommandHandler<UpdateSystemDescriptionCommand> {
    public UpdateSystemDescriptionCommandHandler(CurrentSystemRegisterState systemRegisterState, SystemDescriptionUpdatedEventHandler handler, CurrentStateCalculator calculator) {
        super(systemRegisterState, handler, calculator);
    }
}
