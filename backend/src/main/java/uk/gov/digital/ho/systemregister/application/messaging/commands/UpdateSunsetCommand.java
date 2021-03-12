package uk.gov.digital.ho.systemregister.application.messaging.commands;

import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.AdditionalInformation;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_SystemEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDate;

import static uk.gov.digital.ho.systemregister.util.NullSafeUtils.safelyTrimmed;

public class UpdateSunsetCommand implements UpdateCommand {
    private final int id;
    @Valid
    public final Sunset sunset;
    private final SR_Person author;
    private final Instant timestamp;

    public UpdateSunsetCommand(int id, Sunset sunset, SR_Person author, Instant timestamp) {
        this.id = id;
        this.sunset = sunset;
        this.author = author;
        this.timestamp = timestamp;
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
    public int getId() {
        return id;
    }

    @Override
    public SR_SystemEvent toEvent() {
        return null;
    }

    @Override
    public void ensureCommandUpdatesSystem(SR_System system) throws CommandHasNoEffectException {

    }

    public static class Sunset {
        private final LocalDate date;
        @AdditionalInformation
        private final String additionalInformation;


        public Sunset(LocalDate date, String additionalInformation) {
            this.date = date;
            this.additionalInformation = safelyTrimmed(additionalInformation);
        }
    }
}
