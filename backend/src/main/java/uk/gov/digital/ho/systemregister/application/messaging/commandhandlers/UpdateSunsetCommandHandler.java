package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateSunsetCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.SunsetUpdatedEventHandler;

import javax.inject.Singleton;

@Singleton
public class UpdateSunsetCommandHandler extends AbstractUpdateCommandHandler<UpdateSunsetCommand> {
    public UpdateSunsetCommandHandler(
            CurrentSystemRegisterState systemRegisterState,
            SunsetUpdatedEventHandler eventHandler,
            CurrentStateCalculator currentStateCalculator) {
        super(systemRegisterState, eventHandler, currentStateCalculator);
    }
}
