package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import com.google.common.eventbus.Subscribe;
import org.jboss.logging.Logger;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.MissingAuthorException;
import uk.gov.digital.ho.systemregister.application.messaging.SR_EventBus;
import uk.gov.digital.ho.systemregister.application.messaging.commands.AddSystemCommand;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAddedEvent;
import uk.gov.digital.ho.systemregister.domain.AddSystemResult;
import uk.gov.digital.ho.systemregister.domain.CHANGE;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.domain.SystemRegister;

import java.util.List;

public final class AddSystemCommandHandler {
    static final Logger LOG = Logger.getLogger(AddSystemCommandHandler.class);

    SR_EventBus eventBus;
    CurrentSystemRegisterState currentRegisterState;

    public AddSystemCommandHandler(SR_EventBus eventBus, CurrentSystemRegisterState currentRegisterState) {
        this.eventBus = eventBus;
        this.currentRegisterState = currentRegisterState;
    }

    @Subscribe
    public void handle(AddSystemCommand cmd) throws MissingAuthorException {
        List<SR_System> systems = currentRegisterState.getSystems().systems;
        SystemRegister systemRegister = new SystemRegister(systems);
        AddSystemResult result = systemRegister.addSystem(cmd.systemData);
        if (result.result == CHANGE.ADDED) {
            SystemAddedEvent event = new SystemAddedEvent(result.system, cmd.author);
            eventBus.publish(event);
        }
        if (result.result == CHANGE.DUPLICATE) {
            LOG.warn("Attempted to add a duplicate system: " + cmd.systemData.name);
        }
    }
}
