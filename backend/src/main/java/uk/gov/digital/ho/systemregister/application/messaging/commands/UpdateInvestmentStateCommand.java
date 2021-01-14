package uk.gov.digital.ho.systemregister.application.messaging.commands;

import com.google.common.base.Objects;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.events.InvestmentStateUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.Instant;

public class UpdateInvestmentStateCommand implements Command {
    private final int id;
    @Pattern(regexp = "evergreen|invest|maintain|sunset|decommissioned|cancelled|unknown", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Investment state must be one of the following values: evergreen, invest, maintain, sunset, decommissioned, cancelled, or unknown")
    private final String investmentState;
    @NotNull
    private final SR_Person author;
    @NotNull
    private final Instant timestamp;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public UpdateInvestmentStateCommand(int id, String investmentState, SR_Person author, Instant timestamp) {
        this.id = id;
        this.investmentState = investmentState == null ? null : investmentState.trim();
        this.author = author;
        this.timestamp = timestamp;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public SR_Person getAuthor() {
        return author;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    public InvestmentStateUpdatedEvent toEvent() {
        return new InvestmentStateUpdatedEvent(id, investmentState, author, timestamp);
    }

    @Override
    public void ensureCommandUpdatesSystem(SR_System system) throws CommandHasNoEffectException {
        if (!willUpdate(system)) {
            throw new CommandHasNoEffectException("investment state is the same: " + investmentState);
        }
    }

    public boolean willUpdate(SR_System system) {
        return !Objects.equal(investmentState, system.investmentState);
    }
}
