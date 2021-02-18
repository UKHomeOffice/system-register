package uk.gov.digital.ho.systemregister.domain;

import uk.gov.digital.ho.systemregister.application.messaging.commandhandlers.CommandProcessingException;

public class SystemNameNotUniqueException extends CommandProcessingException {
    @SuppressWarnings("CdiInjectionPointsInspection")
    public SystemNameNotUniqueException(String name) {
        super("There is already a system called " + name);
    }
}
