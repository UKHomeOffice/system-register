package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import org.jboss.logging.Logger;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.messaging.commands.AddSystemCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.SystemAddedEventHandler;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAddedEvent;
import uk.gov.digital.ho.systemregister.domain.*;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public final class AddSystemCommandHandler {
    static final Logger LOG = Logger.getLogger(AddSystemCommandHandler.class);

    private final SystemAddedEventHandler eventHandler;

    CurrentSystemRegisterState currentRegisterState;

    public AddSystemCommandHandler(CurrentSystemRegisterState currentRegisterState, SystemAddedEventHandler eventHandler) {
        this.currentRegisterState = currentRegisterState;
        this.eventHandler = eventHandler;
    }

    public void handle(AddSystemCommand command) throws SystemNameNotUniqueException {
        List<SR_System> systems = currentRegisterState.getCurrentState().getSystems();
        SystemRegister systemRegister = new SystemRegister(systems);
        AddSystemResult result = systemRegister.addSystem(command.systemData);
        if (result.result == Change.ADDED) {
            SystemAddedEvent event = new SystemAddedEvent(result.system, command.author);
            eventHandler.handle(event);
        }
        if (result.result == Change.DUPLICATE) {
            throw new SystemNameNotUniqueException(command.systemData.name);
        }
    }
}
