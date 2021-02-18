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
import java.util.List;

@Singleton
public class AddSystemCommandHandler {
    private final SystemAddedEventHandler eventHandler;
    private final CurrentSystemRegisterState currentRegisterState;

    public AddSystemCommandHandler(CurrentSystemRegisterState currentRegisterState, SystemAddedEventHandler eventHandler) {
        this.currentRegisterState = currentRegisterState;
        this.eventHandler = eventHandler;
    }

    public Tuple2<SR_System, UpdateMetadata> handle(@Valid AddSystemCommand command) throws SystemNameNotUniqueException {
        List<SR_System> systems = currentRegisterState.getCurrentState().getSystems();
        SystemRegister systemRegister = new SystemRegister(systems);
        AddSystemResult result = systemRegister.addSystem(command.toSystemData());
        if (result.result == Change.ADDED) {
            SystemAddedEvent event = new SystemAddedEvent(result.system, command.getAuthor());
            eventHandler.handle(event);
        }
        if (result.result == Change.DUPLICATE) {
            throw new SystemNameNotUniqueException(command.toSystemData().name);
        }
        return Tuple2.of(result.system, new UpdateMetadata(command.getAuthor(), command.getTimestamp()));
    }
}
