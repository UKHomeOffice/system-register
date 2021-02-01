package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import java.util.Collection;

@SuppressWarnings("CdiInjectionPointsInspection")
public class DuplicateAliasProvidedException extends CommandProcessingException {
    public DuplicateAliasProvidedException(Collection<String> aliases) {
        super("You have entered duplicate aliases: " + String.join(", ", aliases));
    }
}
