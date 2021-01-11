package uk.gov.digital.ho.systemregister.application.messaging.commands;

import com.google.common.base.Objects;
import uk.gov.digital.ho.systemregister.application.messaging.events.InvestmentStateUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.Instant;

public class UpdateInvestmentStateCommand {
    public final int id;
    @Pattern(regexp = "evergreen|invest|maintain|sunset|decommissioned|cancelled", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Investment state must be one of the following values: evergreen, invest, maintain, sunset, decommissioned, cancelled")
    private final String investmentState;
    @NotNull
    public final SR_Person author;
    @NotNull
    public final Instant timestamp;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public UpdateInvestmentStateCommand(SR_Person author, Instant timestamp, int id, String investmentState) {
        this.id = id;
        this.investmentState = investmentState == null ? null : investmentState.trim();
        this.author = author;
        this.timestamp = timestamp;
    }

    public InvestmentStateUpdatedEvent toEvent() {
        return new InvestmentStateUpdatedEvent(id, investmentState, author, timestamp);
    }

    public boolean willUpdate(SR_System system) {
        return !Objects.equal(investmentState, system.investmentState);
    }

}
