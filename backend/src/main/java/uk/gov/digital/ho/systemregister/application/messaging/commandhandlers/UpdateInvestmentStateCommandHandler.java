package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import io.smallrye.mutiny.tuples.Tuple2;
import uk.gov.digital.ho.systemregister.application.eventsourcing.aggregates.CurrentSystemRegisterState;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.CurrentStateCalculator;
import uk.gov.digital.ho.systemregister.application.eventsourcing.calculators.UpdateMetadata;
import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdateInvestmentStateCommand;
import uk.gov.digital.ho.systemregister.application.messaging.eventhandlers.InvestmentStateUpdatedEventHandler;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.domain.SystemRegister;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;

@ApplicationScoped
public class UpdateInvestmentStateCommandHandler {
    private final CurrentSystemRegisterState systemRegisterState;
    private final InvestmentStateUpdatedEventHandler eventHandler;
    private final CurrentStateCalculator calculator;

    public UpdateInvestmentStateCommandHandler(
            CurrentSystemRegisterState systemRegisterState,
            InvestmentStateUpdatedEventHandler eventHandler,
            CurrentStateCalculator calculator) {
        this.systemRegisterState = systemRegisterState;
        this.eventHandler = eventHandler;
        this.calculator = calculator;
    }

    private static void checkInvestmentStateWillBeChanged(UpdateInvestmentStateCommand command, SR_System system)
            throws CommandHasNoEffectException {
        if (!command.willUpdate(system)) {
            throw new CommandHasNoEffectException("investment state is the same: " + system.investmentState);
        }
    }

    public Tuple2<SR_System, UpdateMetadata> handle(@Valid UpdateInvestmentStateCommand command)
            throws NoSuchSystemException, CommandHasNoEffectException {
        SystemRegister systemRegister = new SystemRegister(systemRegisterState.getCurrentState().getSystems());

        SR_System system = systemRegister.getSystemById(command.id)
                .orElseThrow(() -> new NoSuchSystemException(command.id));
        checkInvestmentStateWillBeChanged(command, system);

        var event = command.toEvent();
        eventHandler.handle(event);

        return Tuple2.of(
                calculator.applyUpdateToSystem(system, event),
                new UpdateMetadata(command.author, command.timestamp));
    }
}
