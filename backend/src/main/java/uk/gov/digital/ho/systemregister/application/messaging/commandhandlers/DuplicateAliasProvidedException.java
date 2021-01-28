package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

import java.util.Collection;

@SuppressWarnings("CdiInjectionPointsInspection")
public class DuplicateAliasProvidedException extends CommandProcessingException {
    public DuplicateAliasProvidedException(Collection<String> aliases) {
        super("system aliases contains duplicate value: " + aliases);
    }
}
