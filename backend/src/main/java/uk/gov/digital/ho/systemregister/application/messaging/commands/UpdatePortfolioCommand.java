package uk.gov.digital.ho.systemregister.application.messaging.commands;

import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.events.PortfolioUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Objects;

public class UpdatePortfolioCommand implements Command {
    private final int id;
    @Size(min = 2, message = "You must enter a portfolio.")
    @NotNull
    private final String portfolio;
    @NotNull
    private final SR_Person author;
    @NotNull
    private final Instant timestamp;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public UpdatePortfolioCommand(int id, String portfolio, SR_Person author, Instant timestamp) {
        this.id = id;
        this.portfolio = portfolio == null ? null : portfolio.trim();
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

    public PortfolioUpdatedEvent toEvent() {
        return new PortfolioUpdatedEvent(id, portfolio, author, timestamp);
    }

    @Override
    public void ensureCommandUpdatesSystem(SR_System system) throws CommandHasNoEffectException {
        if (!willUpdate(system)) {
            throw new CommandHasNoEffectException("portfolio is the same: " + portfolio);
        }
    }

    public boolean willUpdate(SR_System system) {
        return !Objects.equals(portfolio, system.portfolio);
    }
}
