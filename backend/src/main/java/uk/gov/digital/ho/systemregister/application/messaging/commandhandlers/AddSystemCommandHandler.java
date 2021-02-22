package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import io.smallrye.mutiny.tuples.Tuple2;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.UpdateMetadata;
import uk.gov.digital.ho.systemregister.application.messaging.commands.AddSystemCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.SystemAddedEventHandler;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAddedEvent;
import uk.gov.digital.ho.systemregister.domain.*;

import javax.inject.Singleton;
import javax.validation.Valid;

@Singleton
public class AddSystemCommandHandler {
    private final SystemAddedEventHandler eventHandler;
    private final CurrentSystemRegisterState currentRegisterState;

    public AddSystemCommandHandler(CurrentSystemRegisterState currentRegisterState, SystemAddedEventHandler eventHandler) {
        this.currentRegisterState = currentRegisterState;
        this.eventHandler = eventHandler;
    }

    public Tuple2<SR_System, UpdateMetadata> handle(@Valid AddSystemCommand command) throws SystemNameNotUniqueException {
        var systemRegister = new SystemRegister(currentRegisterState.getCurrentState().getSystems());
        SR_System system = addSystem(command, systemRegister);

        var event = new SystemAddedEvent(system, command.getAuthor());
        eventHandler.handle(event);

        return Tuple2.of(system, new UpdateMetadata(command.getAuthor(), command.getTimestamp()));
    }

    private SR_System addSystem(AddSystemCommand command, SystemRegister systemRegister) throws SystemNameNotUniqueException {
        try {
            return systemRegister.addSystem(command.toSystemData());
        } catch (DuplicateSystemException e) {
            throw new SystemNameNotUniqueException(e.getSystem().name);
        }
    }
}
