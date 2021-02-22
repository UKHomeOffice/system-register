package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.messaging.commands.AddSystemCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.SystemAddedEventHandler;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAddedEvent;
import uk.gov.digital.ho.systemregister.domain.DuplicateSystemException;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.domain.SystemRegister;

import javax.inject.Singleton;

@Singleton
public class AddSystemCommandHandler extends AbstractCommandHandler<AddSystemCommand> {
    public AddSystemCommandHandler(
            CurrentSystemRegisterState systemRegisterState,
            SystemAddedEventHandler eventHandler) {
        super(systemRegisterState, eventHandler);
    }

    @Override
    protected SR_System findSystem(AddSystemCommand command, SystemRegister systemRegister) throws SystemNameNotUniqueException {
        try {
            return systemRegister.addSystem(command.toSystemData());
        } catch (DuplicateSystemException e) {
            throw new SystemNameNotUniqueException(e.getSystem().name);
        }
    }

    @Override
    protected SR_System applyCommand(AddSystemCommand command, SR_System system) {
        var event = new SystemAddedEvent(system, command.getAuthor());
        eventHandler.handle(event);

        return system;
    }
}
