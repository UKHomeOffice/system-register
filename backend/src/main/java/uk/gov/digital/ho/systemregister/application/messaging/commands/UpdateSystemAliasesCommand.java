package uk.gov.digital.ho.systemregister.application.messaging.commands;

import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandHasNoEffectException;
import uk.gov.digital.ho.systemregister.application.messaging.commands.validation.SystemName;
import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAliasesUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_System;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UpdateSystemAliasesCommand implements Command {
    private final int id;
    @NotNull
    public final List<@SystemName String> aliases;
    @NotNull
    private final SR_Person author;
    @NotNull
    private final Instant timestamp;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public UpdateSystemAliasesCommand(int id, List<String> aliases, SR_Person author, Instant timestamp) {
        List<String> trimmedAliases =  aliases == null
                ? null
                : aliases.stream().map(alias -> alias == null ? null : alias.trim()).collect(Collectors.toList());
        this.id = id;
        this.author = author;
        this.timestamp = timestamp;
        this.aliases = trimmedAliases;
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
    public SystemAliasesUpdatedEvent toEvent() {
        return new SystemAliasesUpdatedEvent(id, Set.copyOf(aliases), author, timestamp);
    }

    public boolean willUpdate(SR_System system) {
        return system.aliases == null
                || aliases.size() != system.aliases.size()
                || !aliases.containsAll(system.aliases);
    }

    @Override
    public void ensureCommandUpdatesSystem(SR_System system) throws CommandHasNoEffectException {
        if (!willUpdate(system)) {
            throw new CommandHasNoEffectException("system aliases are the same: " + system.aliases.toString());
        }
    }
}
