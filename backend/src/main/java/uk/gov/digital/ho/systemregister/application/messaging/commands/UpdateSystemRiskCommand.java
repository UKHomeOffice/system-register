package uk.gov.digital.ho.systemregister.application.messaging.commands;

import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.RiskDoesNotExistException;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.RiskLevel;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.RiskName;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.RiskRationale;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemRiskUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_Risk;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;

import static uk.gov.digital.ho.systemregister.util.NullSafeUtils.safelyTrimmed;

public class UpdateSystemRiskCommand implements UpdateCommand {
    private final int id;
    @NotNull
    @Valid
    public final Risk risk;
    @NotNull
    private final SR_Person author;
    @NotNull
    private final Instant timestamp;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public UpdateSystemRiskCommand(int id, Risk risk, SR_Person author, Instant timestamp) {
        this.id = id;
        this.author = author;
        this.timestamp = timestamp;
        this.risk = risk;
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

    @Override
    public SystemRiskUpdatedEvent toEvent() {
        return new SystemRiskUpdatedEvent(id, risk.toSrRisk(), author, timestamp);
    }

    public boolean willUpdate(SR_System system) {
        SR_Risk existingRisk = system.getRiskByName(risk.name)
                .orElseThrow(() -> new IllegalStateException("No matching risk found"));

        return !Objects.equals(existingRisk.level, risk.level) || !risk.rationale.equals(existingRisk.rationale);
    }

    public void ensureRiskExistsOnSystem(SR_System system) throws RiskDoesNotExistException {
        if (system.getRiskByName(risk.name).isEmpty()) {
            throw new RiskDoesNotExistException(risk.name);
        }
    }

    @Override
    public void ensureCommandUpdatesSystem(SR_System system) throws CommandHasNoEffectException {
        if (!willUpdate(system)) {
            throw new CommandHasNoEffectException("The risk values are unchanged.");
        }
    }

    @SuppressWarnings("CdiInjectionPointsInspection")
    public static class Risk {
        @RiskName
        private final String name;
        @RiskLevel
        private final String level;
        @RiskRationale
        private final String rationale;

        public Risk(String name, String level, String rationale) {
            this.name = safelyTrimmed(name);
            this.level = safelyTrimmed(level);
            this.rationale = safelyTrimmed(rationale);
        }

        SR_Risk toSrRisk() {
            return new SR_Risk(name, level, rationale);
        }
    }
}
